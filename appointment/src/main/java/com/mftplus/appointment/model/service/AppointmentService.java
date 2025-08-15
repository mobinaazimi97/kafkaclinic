package com.mftplus.appointment.model.service;


import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.model.entity.Appointment;
import com.mftplus.appointment.model.entity.Schedule;

import com.mftplus.appointment.mapper.AppointmentMapper;
import com.mftplus.appointment.model.repository.AppointmentRepository;
import com.mftplus.appointment.model.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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

    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, ScheduleRepository scheduleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.scheduleRepository = scheduleRepository;
    }


    @Transactional
    @CacheEvict(value = "appointments", allEntries = true)
    public AppointmentDto create(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            throw new IllegalArgumentException("AppointmentDto cannot be null");
        }
        if (appointmentDto.getAppointmentDateTime() == null && appointmentDto.getPersianStartDate() != null) {
            appointmentDto.setPersianStartDate(appointmentDto.getPersianStartDate());
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
    @Cacheable(value = "appointments")
    public AppointmentDto getById(UUID id) {
        Appointment appointment = appointmentRepository.findByAppointmentUuid(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return appointmentMapper.toDto(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDto> getByPatientId(UUID patientId) {
        return appointmentMapper.toDtoList(appointmentRepository.findByPatientUuid(patientId));
    }

    @Transactional
    @CacheEvict(value = "appointments", allEntries = true)
    public void logicalRemove(UUID id) {
        Appointment appointment = appointmentRepository.findByAppointmentUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for UUID: " + id));
        appointmentRepository.logicalRemove(appointment.getId());
    }

}
