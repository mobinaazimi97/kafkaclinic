package com.mftplus.appointment.model.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.model.entity.Appointment;
import com.mftplus.appointment.model.entity.Schedule;

import com.mftplus.appointment.mapper.AppointmentMapper;
import com.mftplus.appointment.model.repository.AppointmentRepository;
import com.mftplus.appointment.model.repository.ScheduleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ScheduleRepository scheduleRepository;
    private final PatientService patientService;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    private final ObjectMapper objectMapper;
    private final AppointmentKafkaProducer appointmentKafkaProducer;


    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, ScheduleRepository scheduleRepository, PatientService patientService, ObjectMapper objectMapper, AppointmentKafkaProducer appointmentKafkaProducer) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.scheduleRepository = scheduleRepository;
        this.patientService = patientService;
        this.objectMapper = objectMapper;
        this.appointmentKafkaProducer = appointmentKafkaProducer;
    }


    @PostConstruct
    public void init() {
        log.info("Initializing Appointment Cache On Startup");
        getAll();
        log.info("Appointment Cache Initialization Completed");
    }

    @Transactional
    @CacheEvict(value = "appointments", allEntries = true)
    public AppointmentDto create(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            throw new IllegalArgumentException("AppointmentDto cannot be null");
        }
        if (appointmentDto.getAppointmentDateTime() == null && appointmentDto.getPersianStartDate() != null) {
            appointmentDto.setPersianStartDate(appointmentDto.getPersianStartDate());
            appointmentDto.setDoctorFirstname(appointmentDto.getDoctorFirstname());
            appointmentDto.setDoctorLastname(appointmentDto.getDoctorLastname());
        }

        Schedule schedule = scheduleRepository.findByScheduleUuid(appointmentDto.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with UUID: " + appointmentDto.getScheduleId()));
        if (schedule.isBooked()) {
            throw new IllegalStateException("Schedule is already booked");
        }
        boolean exists = appointmentRepository.existsByScheduleAndAppointmentDateTime(schedule, appointmentDto.getAppointmentDateTime());
        if (exists) {
            throw new DataIntegrityViolationException("This schedule is already booked at the given appointment date and time.");
        }
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setSchedule(schedule);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        schedule.setBooked(true);
        scheduleRepository.save(schedule);

        AppointmentDto resultDto = appointmentMapper.toDto(savedAppointment);
        resultDto.getPersianStartDate();
        return resultDto;
    }

    @Transactional
    @CacheEvict(value = "appointments", allEntries = true)
    public AppointmentDto update(UUID appointmentUuid, AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            throw new IllegalArgumentException("AppointmentDto cannot be null");
        }

        Appointment appointment = appointmentRepository.findByAppointmentUuid(appointmentUuid)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found for UUID: " + appointmentUuid));

        if (appointmentDto.getAppointmentDateTime() == null && appointmentDto.getPersianStartDate() != null) {
            appointmentDto.setPersianStartDate(appointmentDto.getPersianStartDate());
        }

        if (appointmentDto.getAppointmentDateTime() != null
                && !appointmentDto.getAppointmentDateTime().equals(appointment.getAppointmentDateTime())) {

            boolean exists = appointmentRepository.existsByScheduleAndAppointmentDateTime(
                    appointment.getSchedule(), appointmentDto.getAppointmentDateTime());
            if (exists) {
                throw new DataIntegrityViolationException("This schedule is already booked at the given appointment date and time.");
            }
        }

        appointmentMapper.updateFromDto(appointmentDto, appointment);
        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "appointments")
    public List<AppointmentDto> getAll() {
        return appointmentMapper.toDtoList(appointmentRepository.findAll());
    }

    @Transactional(readOnly = true)
//    @Cacheable(value = "appointments", key = "#id")
    public AppointmentDto getById(UUID id) {
        Appointment appointment = appointmentRepository.findByAppointmentUuid(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found by uuid"));
        return appointmentMapper.toDto(appointment);
    }

//    @Transactional(readOnly = true)
//    @Cacheable(value = "appointments", key = "#patientId")
//    public List<AppointmentDto> getByPatientId(UUID patientId) {
//        return appointmentMapper.toDtoList(appointmentRepository.findByPatientUuid(patientId));
//    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getByPatientId(UUID patientId) {
        AppointmentDto appointmentDto = null;
        try {
            ResponseEntity<AppointmentDto> response = patientService.getAppointmentsByPatientId(patientId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                appointmentDto = response.getBody();
                logger.debug("Found Patient Id {}", patientId);
            } else {
                logger.warn("Failed To Get Patient Id , Status :{}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.warn("Could Not Find Patient Id (service might be down): {}", e.getMessage());
        }
        if (appointmentDto == null) {
            appointmentDto = new AppointmentDto();
            appointmentDto.setAppointmentUuid(UUID.randomUUID());
            appointmentDto.setPatientUuid(UUID.randomUUID());
            logger.info("Created placeholder AppointmentDto for Patient {}", patientId);
        }

        try {
//            String appointmentJson = objectMapper.writeValueAsString(appointmentDto);
            appointmentKafkaProducer.sendData(appointmentDto);
            logger.info("Appointment message sent to Kafka for PatientId {}: {}", patientId, appointmentDto);
        } catch (Exception e) {
            logger.error("Failed to send Appointment message to Kafka for patientId {}: {}", patientId, e.getMessage());
        }
        return ResponseEntity.ok(appointmentDto);

    }

    @Transactional
    @CacheEvict(value = "appointments", allEntries = true)
    public void logicalRemove(UUID id) {
        Appointment appointment = appointmentRepository.findByAppointmentUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for UUID: " + id));
        appointmentRepository.logicalRemove(appointment.getId());
    }

}
