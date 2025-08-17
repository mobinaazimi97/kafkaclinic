package com.mftplus.patient.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.mapper.PatientMapper;
import com.mftplus.patient.model.entity.Patient;
import com.mftplus.patient.model.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        log.info("Initializing patients Cache On Startup");
        findAll();
        log.info("patients Cache Initialization Completed");
    }

    @Transactional
    @CacheEvict(value = "patients", allEntries = true)
    public PatientDto savePatient(PatientDto patientDto) {
        UUID patientId = patientDto.getPatientUuid() != null ? patientDto.getPatientUuid() : UUID.randomUUID();
        patientDto.setPatientUuid(patientId);
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

    @Transactional(readOnly = true)
    public List<PatientDto> getByAppointmentId(UUID appointmentId) {
        return patientMapper.toDtoList(patientRepository.findByAppointmentUuid(appointmentId));
    }


    @Transactional
    @Cacheable(value = "patients")
    public PatientDto findById(UUID patientId) {
        Patient patient = patientRepository.findByPatientUuid(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        return patientMapper.toDto(patient);
    }

    @Cacheable(value = "patients")
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

