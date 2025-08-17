package com.mftplus.appointment.model.service;


import com.mftplus.appointment.dto.DoctorDto;
import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.model.entity.Doctor;
import com.mftplus.appointment.model.entity.Schedule;
import com.mftplus.appointment.model.entity.Specialization;
import com.mftplus.appointment.mapper.DoctorMapper;
import com.mftplus.appointment.model.repository.DoctorRepository;
import com.mftplus.appointment.model.repository.ScheduleRepository;
import com.mftplus.appointment.model.repository.SpecializationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecializationRepository specializationRepository;
    private final ScheduleRepository scheduleRepository;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper, SpecializationRepository specializationRepository, ScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.specializationRepository = specializationRepository;
        this.scheduleRepository = scheduleRepository;
    }


    @PostConstruct
    public void init() {
        log.info("Initializing Doctor Cache On Startup");
        findAll();
        log.info("Doctor Cache Initialization Completed");
    }

    @Transactional
    @CacheEvict(value = "doctors", allEntries = true)
    public DoctorDto save(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.toEntity(doctorDto);

        if (doctorDto.getSpecializations() != null) {
            List<Specialization> specializations = new ArrayList<>();
            for (SpecializationDto specDto : doctorDto.getSpecializations()) {
                Specialization specialization = specializationRepository
                        .findByUuId(specDto.getSpecializationUuid())
                        .orElseThrow(() -> new RuntimeException("Specialization not found with id: " + specDto.getSpecializationUuid()));
                specializations.add(specialization);
            }
            doctor.setSpecializations(specializations);

            for (Specialization spec : specializations) {
                if (spec.getDoctors() == null) {
                    spec.setDoctors(new ArrayList<>());
                }
                if (!spec.getDoctors().contains(doctor)) {
                    spec.getDoctors().add(doctor);
                }
            }
        }

        if (doctorDto.getSchedules() != null) {
            List<Schedule> schedules = new ArrayList<>();
            for (ScheduleDto scheduleDto : doctorDto.getSchedules()) {
                Schedule schedule = scheduleRepository
                        .findByScheduleUuid(scheduleDto.getScheduleUuid())
                        .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleDto.getScheduleUuid()));
                schedule.setDoctor(doctor); // ست کردن دکتر در شِدول
                schedules.add(schedule);
            }
            doctor.setSchedules(schedules);
        }

        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    @Transactional
    public DoctorDto update(UUID doctorUuid, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findByDoctorUuid(doctorUuid)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for UUID: " + doctorUuid));

        doctorMapper.updateFromDto(doctorDto, doctor);

        Doctor updated = doctorRepository.save(doctor);

        return doctorMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "doctors")
    public List<DoctorDto> findAll() {
        List<Doctor> doctorList = doctorRepository.findAll();
        return doctorMapper.toDtoList(doctorList);
    }

    @Transactional(readOnly = true)
    public DoctorDto findSchedule(UUID scheduleUuid) {
        Schedule schedule = scheduleRepository.findByScheduleUuid(scheduleUuid)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with UUID: " + scheduleUuid));

        Long scheduleId = schedule.getScheduleId();

        Doctor doctor = doctorRepository.findSchedule(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for schedule ID: " + scheduleId));

        return doctorMapper.toDto(doctor);
    }


    @Transactional
    @Cacheable(value = "doctors")
    public DoctorDto findById(UUID id) {
        Doctor doctor = doctorRepository.findByDoctorUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for UUID: " + id));
        return doctorMapper.toDto(doctor);
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> findByDoctorName(String doctorFirstname, String doctorLastname) {
        List<Doctor> doctorList = doctorRepository.findByDoctorName(doctorFirstname, doctorLastname);
        return doctorMapper.toDtoList(doctorList);
    }

    @Transactional(readOnly = true)
    public List<DoctorDto> findSpecializations() {
        List<Doctor> doctorList = doctorRepository.findSpecializations();
        return doctorMapper.toDtoList(doctorList);
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorWithSpecializations(UUID doctorUuid) {
        Doctor doctor = doctorRepository.findByDoctorUuid(doctorUuid)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with UUID: " + doctorUuid));

        Doctor doctorWithSpecs = doctorRepository.findDoctorWithSpecializationsByDoctorId(doctor.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor with specializations not found for ID: " + doctor.getDoctorId()));

        return doctorMapper.toDto(doctorWithSpecs);
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorWithAvailableSchedules(UUID doctorUuid) {
        Doctor doctor = doctorRepository.findByDoctorUuid(doctorUuid)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with UUID: " + doctorUuid));

        Doctor doctorWithSchedules = doctorRepository.findDoctorWithAvailableSchedules(doctor.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor with available schedules not found for ID: " + doctor.getDoctorId()));

        return doctorMapper.toDto(doctorWithSchedules);
    }


    @Transactional
    @CacheEvict(value = "doctors", allEntries = true)
    public void logicalRemove(UUID doctorUuid) {
        Doctor doctor = doctorRepository.findByDoctorUuid(doctorUuid)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found for UUID: " + doctorUuid));
        doctorRepository.logicalRemove(doctor.getDoctorId());
    }
}

