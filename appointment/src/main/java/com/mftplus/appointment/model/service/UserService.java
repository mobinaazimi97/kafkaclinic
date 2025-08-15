package com.mftplus.appointment.model.service;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.dto.UserDto;
import com.mftplus.appointment.model.entity.Permission;
import com.mftplus.appointment.model.entity.Role;
import com.mftplus.appointment.model.entity.User;
import com.mftplus.appointment.mapper.UserMapper;
import com.mftplus.appointment.model.repository.PermissionRepository;
import com.mftplus.appointment.model.repository.RoleRepository;
import com.mftplus.appointment.model.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();

        if (userDto.getRoles() != null) {
            for (RoleDto roleDto : userDto.getRoles()) {
                Role role;

                if (roleDto.getRoleUuid() != null) {
                    role = roleRepository.findByRoleUuid(roleDto.getRoleUuid()).orElse(null);
                    if (role == null) continue;
                } else {
                    role = Role.builder().roleName(roleDto.getRoleName()).build();
                }

                Set<Permission> permissions = new HashSet<>();
                if (roleDto.getPermissions() != null) {
                    for (PermissionDto permissionDto : roleDto.getPermissions()) {
                        Permission permission;
                        if (permissionDto.getPermUuid() != null) {
                            permission = permissionRepository.findByPermUuid(permissionDto.getPermUuid()).orElse(null);
                            if (permission != null) {
                                permissions.add(permission);
                            }
                        } else {
                            permission = Permission.builder().permissionName(permissionDto.getPermissionName()).build();
                            permissionRepository.save(permission);
                            permissions.add(permission);
                        }
                    }
                    role.setPermissions(permissions);
                }

                roleRepository.save(role);
                roles.add(role);
            }

            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public UserDto update(UUID id, UserDto userDto) {
        User user = userRepository.findByUserUuid(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateFromDto(userDto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Cacheable(value = "users")
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> userMapper.toDto(u))
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "users")
    public UserDto findById(UUID id) {
        User user = userRepository.findByUserUuid(id).orElse(null);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    @Cacheable(value = "users")
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return userMapper.toDto(user);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public Set<UserDto> findByRoleName(String roleName) {
        Set<User> user = userRepository.findByRoles(roleName);
        return userMapper.toDtoSet(user);
    }

    @Transactional
    @Cacheable(value = "users")
    public UserDto findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new EntityNotFoundException("Invalid Username Or Password!: " + username));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users")
    public Set<UserDto> findBPermsOfUser(String permissionName) {
        Set<User> userPerms = userRepository.findBPermsOfUser(permissionName);
        return userMapper.toDtoSet(userPerms);
    }

    @Transactional
    @Cacheable(value = "users")
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userMapper.toDto(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void logicalRemove(UUID id) {
        User user = userRepository.findByUserUuid(id).orElse(null);
        userRepository.logicalRemove(user.getId());
    }
}
