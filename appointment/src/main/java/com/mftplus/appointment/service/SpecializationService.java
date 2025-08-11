package com.mftplus.appointment.service;


import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.entity.Specialization;
import com.mftplus.appointment.mapper.SpecializationMapper;
import com.mftplus.appointment.repository.SpecializationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;

    public SpecializationService(SpecializationRepository specializationRepository, SpecializationMapper specializationMapper) {
        this.specializationRepository = specializationRepository;
        this.specializationMapper = specializationMapper;
    }


    @PostConstruct
    public void init() {
        log.info("Initializing Specialization Cache On Startup");
        findAll();
        log.info("Specialization Cache Initialization Completed");
    }


    @Transactional
    @CacheEvict(value = "specializations", allEntries = true)
    public SpecializationDto save(SpecializationDto specializationDto) {
        Specialization specialization = specializationMapper.toEntity(specializationDto);
        Specialization saved = specializationRepository.save(specialization);
        return specializationMapper.toDto(saved);
    }


    @Transactional
    @CacheEvict(value = "specializations", allEntries = true)
    public SpecializationDto update(UUID specializationId, SpecializationDto specializationDto) {

        Specialization specialization = specializationRepository.findByUuId(specializationId).orElseThrow(() -> new EntityNotFoundException("Spec not found for UUID: " + specializationId));
        specializationMapper.updateFromDto(specializationDto, specialization);
        Specialization updated = specializationRepository.save(specialization);
        return specializationMapper.toDto(updated);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specializations")
    public List<SpecializationDto> findAll() {
        return specializationMapper.toDtoList(specializationRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specializations")
    public SpecializationDto findById(UUID specializationId) {
        Specialization specialization = specializationRepository.findByUuId(specializationId).orElseThrow(() -> new EntityNotFoundException("Spec not found for UUID: " + specializationId));
        return specializationMapper.toDto(specialization);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specializations")
    public List<SpecializationDto> findBySkillName(String skillName) {
        List<Specialization> specializations = specializationRepository.findBySkillName(skillName);
        return specializationMapper.toDtoList(specializations);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "specializations")
    public List<SpecializationDto> findByDoctorNameAndFamily(String doctorFirstname, String doctorLastname) {
        List<Specialization> specializations = specializationRepository.findByDoctorName(doctorFirstname, doctorLastname);
        return specializationMapper.toDtoList(specializations);
    }

    @Cacheable(value = "specializations")
    public void logicalRemove(UUID specializationId) {
        Specialization specialization = specializationRepository.findByUuId(specializationId)
                .orElseThrow(() -> new EntityNotFoundException("Spec not found for UUID: " + specializationId));

        specializationRepository.logicalRemove(specialization.getSpecializationId());
    }
}


