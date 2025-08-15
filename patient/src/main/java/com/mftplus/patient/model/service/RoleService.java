package com.mftplus.patient.model.service;


import com.mftplus.patient.dto.PermissionDto;
import com.mftplus.patient.dto.RoleDto;
import com.mftplus.patient.mapper.RoleMapper;
import com.mftplus.patient.model.entity.Permission;
import com.mftplus.patient.model.entity.Role;
import com.mftplus.patient.model.repository.PermissionRepository;
import com.mftplus.patient.model.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    @CacheEvict(value = "roles_patients", allEntries = true)
    public RoleDto save(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        Set<Permission> permissionSet = new HashSet<>();

        if (roleDto.getPermissions() != null) {
            for (PermissionDto permissionDto : roleDto.getPermissions()) {
                if (permissionDto.getPermUuid() != null) {
                    Permission permission = permissionRepository.findByPermUuid(permissionDto.getPermUuid()).orElse(null);
                    if (permission != null) {
                        permissionSet.add(permission);
                    }
                } else {
                    Permission newPermission = Permission.builder().permissionName(permissionDto.getPermissionName()).build();
                    permissionRepository.save(newPermission);
                    permissionSet.add(newPermission);
                }
            }
            role.setPermissions(permissionSet);
        }
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Transactional
    @CacheEvict(value = "roles_patients", allEntries = true)
    public RoleDto update(UUID id, RoleDto roleDto) {
        Role role = roleRepository.findByRoleUuid(id).orElse(null);
        roleMapper.updateFromDto(roleDto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }


    @Cacheable(value = "roles_patients")
    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(r -> roleMapper.toDto(r))
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "roles_patients")
    public RoleDto findById(UUID id) {
        Role role = roleRepository.findByRoleUuid(id).get();
        return roleMapper.toDto(role);
    }

    @Transactional
    @Cacheable(value = "roles_patients")
    public RoleDto findByRoleName(String name) {
        Role role = roleRepository.findByRoleName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found for role-name: "));
        return roleMapper.toDto(role);

    }

    @Transactional
    @Cacheable(value = "roles_patients")
    public Set<RoleDto> findByPermissions(String permission) {
        Set<Role> role = roleRepository.findByPermissions(permission);
        return roleMapper.toDtoSet(role);
    }


    @Transactional
    @CacheEvict(value = "roles_patients", allEntries = true)
    public void logicalRemove(UUID id) {
        Role role = roleRepository.findByRoleUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found for UUID: " + id));
        roleRepository.logicalRemove(role.getId());
    }
}
