package com.mftplus.patient.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.mapper.PatientMapper;
import com.mftplus.patient.model.entity.Patient;
import com.mftplus.patient.model.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AppointmentService appointmentService;
    private final PatientKafkaProducer patientKafkaProducer;
    private final ObjectMapper objectMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, AppointmentService appointmentService, PatientKafkaProducer patientKafkaProducer, ObjectMapper objectMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.appointmentService = appointmentService;
        this.patientKafkaProducer = patientKafkaProducer;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing patients Cache On Startup");
        findAll();
        logger.info("patients Cache Initialization Completed");
    }

    @Transactional
    @CacheEvict(value = "patients", allEntries = true)
    public PatientDto savePatient(PatientDto patientDto) {
        UUID patientId = patientDto.getPatientUuid() != null ? patientDto.getPatientUuid() : UUID.randomUUID();
        patientDto.setPatientUuid(patientId);
        patientDto.setDoctorFirstname(patientDto.getDoctorFirstname());
        patientDto.setDoctorLastname(patientDto.getDoctorLastname());
        logger.debug("Set patientId: {}", patientId);

        UUID appointmentId = null;

        try {
            ResponseEntity<PatientDto> appointmentResponse = appointmentService.createAppointment(patientDto);
            if (appointmentResponse.getStatusCode().is2xxSuccessful() && appointmentResponse.getBody() != null) {
                appointmentId = appointmentResponse.getBody().getAppointmentUuid();
                logger.debug("Created appointment with ID: {}", appointmentId);
            } else {
                logger.warn("Failed to create appointment: Status {}", appointmentResponse.getStatusCode());
            }
        } catch (Exception e) {
            logger.warn("Could not create appointment (service might be down): {}", e.getMessage());
        }

        if (!patientRepository.existsByPatientUuid(patientId)) {
            Patient patient = patientMapper.toEntity(patientDto);
            patient.setAppointmentUuid(appointmentId);
            Patient saved = patientRepository.save(patient);
            logger.debug("Saved Patient with ID: {}, appointmentId: {}", saved.getPatientUuid(), saved.getAppointmentUuid());

            try {
                String patientJson = objectMapper.writeValueAsString(patientMapper.toDto(saved));
                patientKafkaProducer.send(patientJson);
                logger.info("Patient message sent to Kafka: {}", patientJson);
            } catch (Exception e) {
                logger.error("Failed to send Patient message to Kafka: {}", e.getMessage());
            }
        } else {
            logger.info("Patient with ID {} already exists, skipping creation", patientId);
        }

        return patientDto;
    }


    @Transactional
    @CacheEvict(value = "patients", key = "#patientId")
    public PatientDto updatePatient(UUID patientId, PatientDto dto) {
        Patient patient = patientRepository.findByPatientUuid(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        patientMapper.updateFromDto(dto, patient);
        Patient updated = patientRepository.save(patient);
        return patientMapper.toDto(updated);
    }

    @Transactional
    @Cacheable(value = "patients")
    public List<PatientDto> findAll() {
        return patientMapper.toDtoList(patientRepository.findAll());
    }

//    @Transactional(readOnly = true)
//    public ResponseEntity<?> getByAppointmentId(UUID appointmentId) {
//        try {
//            appointmentService.getAppointmentById(appointmentId).getBody();
//            if (appointmentService.getAppointmentById(appointmentId).getStatusCode().is2xxSuccessful() && appointmentService.getAppointmentById(appointmentId).getBody() != null) {
//                appointmentId = (UUID) appointmentService.getAppointmentById(appointmentId).getBody();
//                logger.debug("found appointment with ID: {}", appointmentId);
//            } else {
//                logger.warn("Failed to find appointment: Status {}", appointmentService.getAppointmentById(appointmentId));
//            }
//        } catch (Exception e) {
//            logger.warn("Could not find appointment (service might be down): {}", e.getMessage());
//        }
//
//        Object patientDto = appointmentService.getAppointmentById(appointmentId).getBody();
//        try {
//            String patientJson = objectMapper.writeValueAsString(patientMapper.toDto((Patient) patientDto));
//            patientKafkaProducer.send(patientJson);
//            logger.info("Patient message sent to Kafka For get AppointmentId : {}", patientJson);
//        } catch (Exception e) {
//            logger.error("Failed to send Patient message to Kafka For get AppointmentId: {}", e.getMessage());
//        }
//        return ResponseEntity.ok(patientDto);
////        return patientMapper.toDtoList(patientRepository.findByAppointmentUuid(appointmentId));
//    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getByAppointmentId(UUID appointmentId) {
        PatientDto patientDto = null;
        try {
            ResponseEntity<PatientDto> response = appointmentService.getAppointmentById(appointmentId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                patientDto = response.getBody();
                logger.debug("Found appointment with ID: {}", appointmentId);
            } else {
                logger.warn("Failed to find appointment: Status {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.warn("Could not find appointment (service might be down): {}", e.getMessage());
        }

        if (patientDto == null) {
            patientDto = new PatientDto();
            patientDto.setPatientUuid(UUID.randomUUID());
            patientDto.setAppointmentUuid(appointmentId);

            //todo
            //------------------------------------------------------------
            patientDto.setFirstName(patientDto.getFirstName());
            patientDto.setLastName(patientDto.getLastName());
            patientDto.setDoctorFirstname(patientDto.getDoctorFirstname());
            patientDto.setDoctorLastname(patientDto.getDoctorLastname());
            patientDto.setNotes(patientDto.getNotes());
            patientDto.setPhone(patientDto.getPhone());
            patientDto.setScheduleId(patientDto.getScheduleId());
            //------------------------------------------------------------
            logger.info("Created placeholder PatientDto for appointmentId {}", appointmentId);
        }

        try {
            String patientJson = objectMapper.writeValueAsString(patientDto);
            patientKafkaProducer.send(patientJson);
            logger.info("Patient message sent to Kafka for appointmentId {}: {}", appointmentId, patientJson);
        } catch (Exception e) {
            logger.error("Failed to send Patient message to Kafka for appointmentId {}: {}", appointmentId, e.getMessage());
        }

        return ResponseEntity.ok(patientDto);
    }


    @Transactional
    @Cacheable(value = "patients")
    public PatientDto findById(UUID patientId) {
        Patient patient = patientRepository.findByPatientUuid(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        return patientMapper.toDto(patient);
    }

    @Cacheable(value = "patients", key = "T(java.util.Objects).hash(#firstName, #lastName)")
    @Transactional(readOnly = true)
    public PatientDto findByFullName(String firstName, String lastName) {
        Patient patient = patientRepository.findByFullName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with full name : "));
        return patientMapper.toDto(patient);

    }

    @Transactional
    @CacheEvict(value = "patients", allEntries = true)
    public void logicalRemove(UUID patientId) {
        Patient patient = patientRepository.findByPatientUuid(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        patientRepository.logicalRemove(patient.getPatientId());

    }

}

