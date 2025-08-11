package com.mftplus.patient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.patient.dto.AppointmentDto;
import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.mapper.PatientMapper;
import com.mftplus.patient.model.Patient;
import com.mftplus.patient.repository.PatientRepository;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public PatientDto updatePatient(UUID patientId, PatientDto dto) {
        Patient patient = patientRepository.findByPatientUuid(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        patientMapper.updateFromDto(dto, patient);
        Patient updated = patientRepository.save(patient);
        return patientMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "patients")
    public List<PatientDto> findAll() {
        return patientMapper.toDtoList(patientRepository.findAll());
    }

    @Transactional
    @CacheEvict(value = "patients", allEntries = true)
    public void logicalRemove(UUID patientId) {
        Patient patient = patientRepository.findByPatientUuid(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));
        patientRepository.logicalRemove(patient.getPatientId());

    }

    @Transactional
    @CacheEvict(value = "patients", allEntries = true)
    public PatientDto savePatient(PatientDto dto) {
        // Step 1: Validate Appointment if exists
        if (dto.getAppointmentId() != null) {
            try {
                ResponseEntity<AppointmentDto> response = appointmentService.getAppointmentById(dto.getAppointmentId());
                if (response.getBody() == null) {
                    throw new IllegalArgumentException("قرار ملاقات با آیدی " + dto.getAppointmentId() + " یافت نشد.");
                }
            } catch (FeignException e) {
                logger.error("خطا در ارتباط با سرویس Appointment: {}", e.getMessage());
                throw new RuntimeException("خطا در بررسی قرار ملاقات: " + e.getMessage());
            }
        }

        // Step 2: Save to database
        Patient patient = patientMapper.toEntity(dto);
        Patient saved = patientRepository.save(patient);
        PatientDto savedDto = patientMapper.toDto(saved);

        // Step 3: Publish to Kafka (non-critical)
        try {
            String patientJson = objectMapper.writeValueAsString(savedDto);
            patientKafkaProducer.send(patientJson);
            logger.info("پیام بیمار به Kafka ارسال شد: {}", patientJson);
        } catch (JsonProcessingException e) {
            logger.error("خطا در تبدیل PatientDto به JSON: {}", e.getMessage());
            // فقط خطا رو لاگ کن ولی Exception نده مگر این موضوع حیاتی باشه
        } catch (Exception e) {
            logger.error("خطای عمومی در ارسال Kafka: {}", e.getMessage());
        }

        return savedDto;
    }


//    @Transactional
//    public PatientDto savePatient(PatientDto dto) {
//        Patient patient = patientMapper.toEntity(dto);
//        Patient saved = patientRepository.save(patient);
//        return patientMapper.toDto(saved);
//    }

//    @Transactional
//    @CacheEvict(value = "patients", allEntries = true)
//    public PatientDto savePatient(PatientDto dto) {
//        if (dto.getAppointmentId() != null) {
//            try {
//                ResponseEntity<AppointmentDto> response = appointmentService.getAppointmentById(dto.getAppointmentId());
//                if (response.getBody() == null) {
//                    throw new IllegalArgumentException("قرار ملاقات با آیدی " + dto.getAppointmentId() + " یافت نشد.");
//                }
//            } catch (FeignException e) {
//                logger.error("خطا در ارتباط با سرویس Appointment: {}", e.getMessage());
//                throw new RuntimeException("خطا در بررسی قرار ملاقات: " + e.getMessage());
//            }
//        }
//
//        // تبدیل DTO به Entity
//        Patient patient = patientMapper.toEntity(dto);
//        // ذخیره در دیتابیس
//        Patient saved = patientRepository.save(patient);
//        // تبدیل به DTO و سپس به JSON برای ارسال به Kafka
//        try {
//            PatientDto savedDto = patientMapper.toDto(saved);
//            String patientJson = objectMapper.writeValueAsString(savedDto);
//            patientKafkaProducer.send(patientJson);
//        } catch (JsonProcessingException e) {
//            logger.error("خطا در تبدیل PatientDto به JSON: {}", e.getMessage());
//            throw new RuntimeException("خطا در ارسال رویداد به Kafka: " + e.getMessage());
//        }
//        return patientMapper.toDto(saved);
//
//    }


    @Transactional(readOnly = true)
    public List<PatientDto> getByAppointmentId(UUID appointmentId) {
        return patientMapper.toDtoList(patientRepository.findByAppointmentId(appointmentId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "patients")
    public PatientDto findById(UUID patientId) {
        Patient patient = patientRepository.findByPatientUuid(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        return patientMapper.toDto(patient);

//        if (patient.getAppointmentId() != null) {
//            AppointmentDto appointment = appointmentService.getAppointmentById(patient.getAppointmentId()).getBody();
////            dto.setAppointmentDetails(appointment);
//            dto.setAppointmentId(appointment.getId());

//        }
    }

//    @Transactional(readOnly = true)
//    public ResponseEntity<PatientDto> getPatientWithAppointment(Long patientId) {
//        logger.info("در حال دریافت اطلاعات بیمار با شناسه: {}", patientId);
//
//        // پیدا کردن بیمار
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> {
//                    logger.error("بیمار با شناسه {} یافت نشد", patientId);
//                    return new RuntimeException("بیمار یافت نشد");
//                });
//
//        // تبدیل به DTO
//        PatientDto patientDto = PatientDto.builder()
//                .patientId(patient.getPatientId())
//                .firstName(patient.getFirstName())
//                .lastName(patient.getLastName())
//                .age(patient.getAge())
//                .phone(patient.getPhone())
//                .appointmentId(patient.getAppointmentId())
//                .build();
//
//        // فراخوانی FeignClient برای گرفتن اطلاعات نوبت
//        if (patient.getAppointmentId() != null) {
//            try {
//                logger.info("در حال دریافت نوبت با شناسه: {}", patient.getAppointmentId());
//                ResponseEntity<AppointmentDto> response = appointmentService.getAppointmentById(patient.getAppointmentId());
//                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
////                    patientDto.setAppointmentDetails(response.getBody());
//                    logger.info("نوبت با موفقیت دریافت شد: {}", response.getBody().getId());
//                } else {
//                    logger.warn("نوبت با شناسه {} یافت نشد یا پاسخ ناموفق بود", patient.getAppointmentId());
////                    patientDto.setAppointmentDetails(null);
//                }
//            } catch (Exception e) {
//                logger.error("خطا در دریافت نوبت با شناسه {}: {}", patient.getAppointmentId(), e.getMessage());
////                patientDto.setAppointmentDetails(null);
//            }
//        } else {
//            logger.info("بیمار با شناسه {} نوبت مرتبطی ندارد", patientId);
////            patientDto.setAppointmentDetails(null);
//        }
//
//        // ارسال رویداد به Kafka
//        patientKafkaProducer.sendPatientEvent(patientDto);
//
//        return ResponseEntity.ok(patientDto); // برگردوندن ResponseEntity با کد 200
//    }
//    @Transactional
//    public PatientDto savePatientAndRequestAppointment(PatientDto dto) {
//        if (dto == null) {
//            throw new IllegalArgumentException("PatientDto must not be null");
//        }
//
//        Patient patient = patientMapper.toEntity(dto);
//        Patient savedPatient = patientRepository.save(patient);
//
//        if (dto.getAppointmentDetails() != null) {
//            AppointmentDto appointmentRequest = dto.getAppointmentDetails();
//            appointmentRequest.setPatientId(savedPatient.getPatientId());
//
//            try {
//                ResponseEntity<AppointmentDto> response = appointmentService.createAppointment(appointmentRequest);
//                AppointmentDto createdAppointment = response.getBody();
//
//                if (createdAppointment != null && createdAppointment.getId() != null) {
//                    savedPatient.setAppointmentId(createdAppointment.getId());
//                    savedPatient = patientRepository.save(savedPatient);
//
//                    dto.setAppointmentDetails(createdAppointment);
//                }
//
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to create appointment: " + e.getMessage(), e);
//            }
//        }
//        PatientDto result = patientMapper.toDto(savedPatient);
//        result.setAppointmentDetails(dto.getAppointmentDetails()); // صرفاً برای بازگشت اطلاعات کامل
//        return result;
//    }
//


}

//-----------------------------------------------------------------------
//Kafka :
//    @Transactional
//    public PatientDto savePatient(PatientDto dto) {
//        if (dto == null) {
//            logger.error("PatientDto is null");
//            throw new IllegalArgumentException("PatientDto must not be null");
//        }
//
//        // تبدیل DTO به Entity و ذخیره Patient
//        Patient patient = patientMapper.toEntity(dto);
//        Patient savedPatient = patientRepository.save(patient);
//        logger.info("Patient saved with ID: {}", savedPatient.getPatientId());
//
//        // ساخت و ارسال PatientCreatedEvent با SuperBuilder
//        PatientCreatedEvent event = PatientCreatedEvent.builder()
//                .patientId(savedPatient.getPatientId())
//                .firstName(savedPatient.getFirstName())
//                .lastName(savedPatient.getLastName())
//                .age(savedPatient.getAge())
//                .phone(savedPatient.getPhone())
//                .eventType("PATIENT_CREATED")
//                .eventTimestamp(java.time.LocalDateTime.now())
//                .build();
//        try {
//            patientKafkaTemplate.send("patient-events", event);
//            logger.info("PatientCreatedEvent sent for patientId: {}", savedPatient.getPatientId());
//        } catch (Exception e) {
//            logger.error("Failed to send PatientCreatedEvent for patientId: {}", savedPatient.getPatientId(), e);
//            // می‌تونید به Dead Letter Queue بفرستید
//        }
//
//        return patientMapper.toDto(savedPatient);
//    }
//
//    //Kafka :
//    @Transactional
//    public PatientDto savePatientAndRequestAppointment(PatientDto dto) {
//        if (dto == null) {
//            logger.error("PatientDto is null");
//            throw new IllegalArgumentException("PatientDto must not be null");
//        }
//
//        // تبدیل DTO به Entity و ذخیره Patient
//        Patient patient = patientMapper.toEntity(dto);
//        Patient savedPatient = patientRepository.save(patient);
//        logger.info("Patient saved with ID: {}", savedPatient.getPatientId());
//
//        // ساخت و ارسال PatientCreatedEvent با SuperBuilder
//        PatientCreatedEvent patientEvent = PatientCreatedEvent.builder()
//                .patientId(savedPatient.getPatientId())
//                .firstName(savedPatient.getFirstName())
//                .lastName(savedPatient.getLastName())
//                .age(savedPatient.getAge())
//                .phone(savedPatient.getPhone())
//                .eventType("PATIENT_CREATED")
//                .eventTimestamp(java.time.LocalDateTime.now())
//                .build();
//        try {
//            patientKafkaTemplate.send("patient-events", patientEvent);
//            logger.info("PatientCreatedEvent sent for patientId: {}", savedPatient.getPatientId());
//        } catch (Exception e) {
//            logger.error("Failed to send PatientCreatedEvent for patientId: {}", savedPatient.getPatientId(), e);
//        }
//
//        // مدیریت درخواست Appointment
//        if (dto.getAppointmentDetails() != null) {
//            // تنظیم اطلاعات در AppointmentDto با SuperBuilder
//            AppointmentDto appointmentRequest = AppointmentDto.builder()
//                    .patientId(savedPatient.getPatientId())
//                    .appointmentDateTime(dto.getAppointmentDetails().getAppointmentDateTime())
//                    .notes(dto.getAppointmentDetails().getNotes())
//                    .patientFirstName(savedPatient.getFirstName())
//                    .patientLastName(savedPatient.getLastName())
//                    .patientPhone(savedPatient.getPhone())
//                    .build();
//            // ساخت و ارسال AppointmentRequestEvent با SuperBuilder
//            AppointmentRequestEvent appointmentEvent = AppointmentRequestEvent.builder()
//                    .patientId(savedPatient.getPatientId())
//                    .appointmentDateTime(appointmentRequest.getAppointmentDateTime())
//                    .notes(appointmentRequest.getNotes())
//                    .patientFirstName(savedPatient.getFirstName())
//                    .patientLastName(savedPatient.getLastName())
//                    .patientPhone(savedPatient.getPhone())
//                    .eventType("APPOINTMENT_REQUEST")
//                    .eventTimestamp(java.time.LocalDateTime.now())
//                    .build();
//            try {
//                appointmentKafkaTemplate.send("appointment-requests", appointmentEvent);
//                logger.info("AppointmentRequestEvent sent for patientId: {}", savedPatient.getPatientId());
//            } catch (Exception e) {
//                logger.error("Failed to send AppointmentRequestEvent for patientId: {}", savedPatient.getPatientId(), e);
//            }
//
//            // موقتاً از FeignClient برای سازگاری
//            try {
//                ResponseEntity<AppointmentDto> response = appointmentService.createAppointment(appointmentRequest);
//                AppointmentDto createdAppointment = response.getBody();
//                if (createdAppointment != null && createdAppointment.getId() != null) {
//                    savedPatient.setAppointmentId(createdAppointment.getId());
//                    savedPatient = patientRepository.save(savedPatient);
//                    logger.info("AppointmentId {} saved for patientId: {}", createdAppointment.getId(), savedPatient.getPatientId());
//                    dto.setAppointmentDetails(createdAppointment);
//                }
//            } catch (Exception e) {
//                logger.warn("Failed to create appointment via FeignClient for patientId: {}. Relying on Kafka.", savedPatient.getPatientId(), e);
//            }
//        }
//
//        PatientDto result = patientMapper.toDto(savedPatient);
//        result.setAppointmentDetails(dto.getAppointmentDetails());
//        return result;
//    }


//Kafka :
//    @Transactional
//    public PatientDto savePatient(PatientDto dto) {
//        Patient patient = patientMapper.toEntity(dto);
//        Patient saved = patientRepository.save(patient);
//
////         ارسال Event به Kafka
//        PatientCreatedEvent event = new PatientCreatedEvent(
//                saved.getPatientId(),
//                saved.getFirstName(),
//                saved.getLastName(),
//                saved.getAge(),
//                saved.getPhone()
//
//        );
//        patientKafkaTemplate.send("patient-events", event);
//
//        return patientMapper.toDto(saved);
//    }

//Kafka :


//Kafka :
//    @Transactional
//    public PatientDto savePatientAndRequestAppointment(PatientDto dto) {
//        if (dto == null) {
//            throw new IllegalArgumentException("PatientDto must not be null");
//        }
//
//        Patient patient = patientMapper.toEntity(dto);
//        Patient savedPatient = patientRepository.save(patient);
//
//        if (dto.getAppointmentDetails() != null) {
//            AppointmentDto appointmentRequest = dto.getAppointmentDetails();
//            appointmentRequest.setPatientId(savedPatient.getPatientId());
//
//            try {
//                // موقتاً از FeignClient استفاده می‌کنیم
//                ResponseEntity<AppointmentDto> response = appointmentService.createAppointment(appointmentRequest);
//                AppointmentDto createdAppointment = response.getBody();
//
//                if (createdAppointment != null && createdAppointment.getId() != null) {
//                    savedPatient.setAppointmentId(createdAppointment.getId());
//                    savedPatient = patientRepository.save(savedPatient);
//                    dto.setAppointmentDetails(createdAppointment);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to create appointment: " + e.getMessage(), e);
//            }
//        }
//        PatientDto result = patientMapper.toDto(savedPatient);
//        result.setAppointmentDetails(dto.getAppointmentDetails());
//        return result;
//    }

//---------------------------------------------------------------------------

//    private final PatientKafkaPublisher patientKafkaPublisher;


//Kafka :
//    @Transactional
//    public PatientDto savePatient(PatientDto dto) {
//        Patient patient = patientMapper.toEntity(dto);
//        Patient saved = patientRepository.save(patient);
//        PatientDto savedDto = patientMapper.toDto(saved);
//
//        // ساختن event کامل
//        PatientEvent event = PatientEvent.builder()
//                .eventType("PATIENT_CREATED")
//                .payload(savedDto)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        // ارسال event به Kafka
//        patientKafkaPublisher.sendPatientCreatedEvent(event);
//
//        return savedDto;
//    }


//Kafka based :
//    public PatientDto findById(Long patientId) {
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
//
//        PatientDto dto = patientMapper.toDto(patient);
//
//        // فقط شناسه وقت ملاقات را برگردان
//        if (patient.getAppointmentId() != null) {
//            AppointmentDtold appointmentSummary = new AppointmentDtold();
//            appointmentSummary.setId(patient.getAppointmentId());
//            dto.setAppointmentDetails(appointmentSummary);
//        }
//
//        return dto;
//    }

//Kafka :
//    @Transactional
//    public PatientDto savePatientAndRequestAppointment(PatientDto dto) {
//        if (dto == null) {
//            throw new IllegalArgumentException("PatientDto must not be null");
//        }
//
//        // 1. ذخیره بیمار در دیتابیس
//        Patient patient = patientMapper.toEntity(dto);
//        Patient savedPatient = patientRepository.save(patient);
//        PatientDto savedPatientDto = patientMapper.toDto(savedPatient);
//
//        // 2. ساخت رویداد Kafka
//        PatientEvent.PatientEventBuilder eventBuilder = PatientEvent.builder()
//                .payload(savedPatientDto)
//                .timestamp(LocalDateTime.now());
//
//        // اگر درخواست وقت ملاقات همراه بیمار بود
//        if (dto.getAppointmentDetails() != null) {
//            AppointmentDtold appointmentDto = dto.getAppointmentDetails();
//            appointmentDto.setPatientId(savedPatient.getPatientId());
//            eventBuilder
//                    .eventType("PATIENT_CREATED_WITH_APPOINTMENT_REQUEST")
//                    .appointmentRequest(appointmentDto);
//        } else {
//            eventBuilder.eventType("PATIENT_CREATED");
//        }
//
//        // 3. ارسال event به Kafka
//        patientKafkaPublisher.sendPatientCreatedEvent(eventBuilder.build());
//
//        // 4. بازگشت DTO ذخیره‌شده
//        return savedPatientDto;
//    }

