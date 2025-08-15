package com.mftplus.appointment.model.service;


import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.model.entity.Doctor;
import com.mftplus.appointment.model.entity.Schedule;
import com.mftplus.appointment.model.entity.Specialization;
import com.mftplus.appointment.mapper.ScheduleMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper, DoctorRepository doctorRepository, SpecializationRepository specializationRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }


    @PostConstruct
    public void init() {
        log.info("Initializing Schedule Cache On Startup");
        findAll();
        log.info("Schedule Cache Initialization Completed");
    }

    @Transactional
    @CacheEvict(value = "schedules", allEntries = true)
    public ScheduleDto save(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        Schedule saved = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(saved);
    }


    @Transactional
    @CacheEvict(value = "schedules", allEntries = true)
    public List<ScheduleDto> createSchedulesForDoctor(UUID doctorUuid, LocalDateTime start, LocalDateTime end, int appointmentDurationMin) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end datetime must not be null");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start datetime must be before end datetime");
        }
        if (appointmentDurationMin <= 0) {
            throw new IllegalArgumentException("Appointment duration must be positive");
        }

        Doctor doctor = doctorRepository.findByDoctorUuid(doctorUuid)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with UUID: " + doctorUuid));

        List<Schedule> schedules = new ArrayList<>();
        LocalDateTime current = start;

        while (!current.isAfter(end.minusMinutes(appointmentDurationMin))) {
            Schedule schedule = Schedule.builder()
                    .doctor(doctor)
                    .startDateTime(current)
                    .endDateTime(current.plusMinutes(appointmentDurationMin))
                    .appointmentDurationMin(appointmentDurationMin)
                    .isBooked(false)
                    .deleted(false)
                    .build();
            schedules.add(schedule);

            current = current.plusMinutes(appointmentDurationMin);
        }

        List<Schedule> savedSchedules = scheduleRepository.saveAll(schedules);
        return scheduleMapper.toDtoList(savedSchedules);
    }

    @Transactional
    @CacheEvict(value = "schedules", allEntries = true)
    public ScheduleDto update(UUID scheduleId, ScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findByScheduleUuid(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with UUID: " + scheduleId));
        scheduleMapper.updateFromDto(scheduleDto, schedule);
        Schedule updated = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(updated);
    }

    @CacheEvict(value = "schedules", allEntries = true)
    public void logicalRemove(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleUuid(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with UUID: " + scheduleId));
        scheduleRepository.logicalRemove(schedule.getScheduleId());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "schedules")
    public List<ScheduleDto> findAll() {
        return scheduleMapper.toDtoList(scheduleRepository.findAll());
    }


    @Transactional(readOnly = true)
    public ScheduleDto getById(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findByScheduleUuid(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with UUID: " + scheduleId));
        return scheduleMapper.toDto(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> findAvailableSchedulesBySpecialization(UUID specializationUuid) {
        Specialization specialization = specializationRepository.findByUuId(specializationUuid)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found for UUID: " + specializationUuid));

        Long specializationId = specialization.getSpecializationId();
        List<Schedule> schedules = scheduleRepository.findAvailableSchedulesBySpecialization(specializationId);
        return scheduleMapper.toDtoList(schedules);
    }

}
