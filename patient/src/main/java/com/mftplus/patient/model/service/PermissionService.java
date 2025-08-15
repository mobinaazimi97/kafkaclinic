package com.mftplus.patient.model.service;


import com.mftplus.patient.dto.PermissionDto;
import com.mftplus.patient.mapper.PermissionMapper;
import com.mftplus.patient.model.entity.Permission;
import com.mftplus.patient.model.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }


    @Transactional
    @CacheEvict(value = "permissions_patients", allEntries = true)
    public PermissionDto save(PermissionDto permissionDto) {
        Permission permission = permissionMapper.toEntity(permissionDto);
        Permission saved = permissionRepository.save(permission);
        return permissionMapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(value = "permissions_patients", allEntries = true)
    public PermissionDto update(UUID id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findByPermUuid(id).orElse(null);
        permissionMapper.updateFromDto(permissionDto, permission);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Transactional
    @Cacheable(value = "permissions_patients")
    public List<PermissionDto> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(p -> permissionMapper.toDto(p))
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "permissions_patients")
    public PermissionDto findById(UUID id) {
        Permission permission = permissionRepository.findByPermUuid(id).orElse(null);
        return permissionMapper.toDto(permission);
    }

    @Transactional
    @Cacheable(value = "permissions_patients")
    public PermissionDto findByPermissionName(String name) {
        Permission permission = permissionRepository.findByPermissionName(name).orElse(null);
        return permissionMapper.toDto(permission);
    }

    @Transactional
    @CacheEvict(value = "permissions_patients", allEntries = true)
    public void logicalRemove(UUID id) {
        Permission permission = permissionRepository.findByPermUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found for UUID: " + id));

        permissionRepository.logicalRemove(permission.getId());
    }
}
