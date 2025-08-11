package com.mftplus.appointment.service;

import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.entity.Permission;
import com.mftplus.appointment.entity.Role;
import com.mftplus.appointment.mapper.RoleMapper;
import com.mftplus.appointment.repository.PermissionRepository;
import com.mftplus.appointment.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public List<RoleDto> saveAll(List<RoleDto> roleDtos) {
        roleDtos.forEach(roleDto -> {
            RoleDto savedRole = save(roleDto);
        });
        return roleDtos;
    }

    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(r -> roleMapper.toDto(r))
                .collect(Collectors.toList());
    }

    public RoleDto findById(UUID id) {
        Role role = roleRepository.findByRoleUuid(id).get();
        return roleMapper.toDto(role);
    }

    @Transactional
    public RoleDto update(UUID id, RoleDto roleDto) {
        Role role = roleRepository.findByRoleUuid(id).orElse(null);
        roleMapper.updateFromDto(roleDto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Transactional
    public void logicalRemove(UUID id) {
        Role role = roleRepository.findByRoleUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found for UUID: " + id));
        roleRepository.logicalRemove(role.getId());
    }
}
