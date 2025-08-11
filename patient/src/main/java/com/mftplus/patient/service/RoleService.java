package com.mftplus.patient.service;


import com.mftplus.patient.dto.PermissionDto;
import com.mftplus.patient.dto.RoleDto;
import com.mftplus.patient.mapper.RoleMapper;
import com.mftplus.patient.model.Permission;
import com.mftplus.patient.model.Role;
import com.mftplus.patient.repository.PermissionRepository;
import com.mftplus.patient.repository.RoleRepository;
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

//        Set<Permission> permissions = role.getPermissionSet().stream()
//                .map(permission -> permissionRepository.findByPermissionName(permission.getPermissionName())
//                        .orElseThrow(() -> new IllegalArgumentException("Permission not found : " + permission.getPermissionName())))
//                .collect(Collectors.toSet());
//        role.setPermissionSet(permissions);
//        Role savedRole = roleRepository.save(role);
//        return roleMapper.toDto(savedRole, "Role");
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
