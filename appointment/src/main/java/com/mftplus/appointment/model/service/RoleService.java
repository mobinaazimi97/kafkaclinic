package com.mftplus.appointment.model.service;

import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.model.entity.Permission;
import com.mftplus.appointment.model.entity.Role;
import com.mftplus.appointment.mapper.RoleMapper;
import com.mftplus.appointment.model.repository.PermissionRepository;
import com.mftplus.appointment.model.repository.RoleRepository;
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
    @CacheEvict(value = "roles", allEntries = true)
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
    @CacheEvict(value = "roles", allEntries = true)
    public RoleDto update(UUID id, RoleDto roleDto) {
        Role role = roleRepository.findByRoleUuid(id).orElse(null);
        roleMapper.updateFromDto(roleDto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Cacheable(value = "roles")
    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(r -> roleMapper.toDto(r))
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "roles")
    public RoleDto findById(UUID id) {
        Role role = roleRepository.findByRoleUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Role Not Found By UUID: " + id));

        return roleMapper.toDto(role);
    }

    @Transactional
    @Cacheable(value = "roles")
    public RoleDto findByRoleName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role Not Found By Name "));
        return roleMapper.toDto(role);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "roles")
    public Set<RoleDto> findByPermissionName(String permissionName) {
        Set<Role> roles = roleRepository.findByPermissions(permissionName);
        return roleMapper.toDtoSet(roles);
    }

    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public void logicalRemove(UUID id) {
        Role role = roleRepository.findByRoleUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found by UUID to delete : " + id));
        roleRepository.logicalRemove(role.getId());
    }
}
