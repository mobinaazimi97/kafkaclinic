package com.mftplus.appointment.service;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.entity.Permission;
import com.mftplus.appointment.mapper.PermissionMapper;
import com.mftplus.appointment.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public PermissionDto save(PermissionDto permissionDto) {
        Permission permission = permissionMapper.toEntity(permissionDto);
        Permission saved = permissionRepository.save(permission);
        return permissionMapper.toDto(saved);
    }

    @Transactional
    public List<PermissionDto> saveAll(List<PermissionDto> permissionDtos) {
        permissionDtos.forEach(permissionDto -> {
            PermissionDto savedPermission = save(permissionDto);
        });
        return permissionDtos;
    }

    public List<PermissionDto> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(p -> permissionMapper.toDto(p))
                .collect(Collectors.toList());
    }

    public PermissionDto findById(UUID id) {
        Permission permission = permissionRepository.findByPermUuid(id).orElse(null);
        return permissionMapper.toDto(permission);
    }

    @Transactional
    public PermissionDto update(UUID id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findByPermUuid(id).orElse(null);
        permissionMapper.updateFromDto(permissionDto, permission);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Transactional
    public void logicalRemove(UUID id) {
        Permission permission = permissionRepository.findByPermUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found for UUID: " + id));

        permissionRepository.logicalRemove(permission.getId());
    }
}
