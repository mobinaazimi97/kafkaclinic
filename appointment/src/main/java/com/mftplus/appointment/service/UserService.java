package com.mftplus.appointment.service;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.dto.UserDto;
import com.mftplus.appointment.entity.Permission;
import com.mftplus.appointment.entity.Role;
import com.mftplus.appointment.entity.User;
import com.mftplus.appointment.mapper.UserMapper;
import com.mftplus.appointment.repository.PermissionRepository;
import com.mftplus.appointment.repository.RoleRepository;
import com.mftplus.appointment.repository.UserRepository;
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


    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return userMapper.toDto(user);
    }

//    @Transactional
//    public UserDto save(UserDto userDto) {
//        User user = userMapper.toEntity(userDto, "User");
//
//        Set<Role> roles = new HashSet<>();
//
//        if (userDto.getRoleSet() != null) {
//            for (RoleDto roleDto : userDto.getRoleSet()) {
//                if (roleDto.getId() != null) {
//                    Long id = uuidMapper.map(roleDto.getId(), "Role");
//                    Role role = roleRepository.findById(id).orElse(null);
//                    if (role != null) {
//                        roles.add(role);
//                    }
//                } else {
//                    Role newRole = Role.builder().roleName(roleDto.getRoleName()).build();
//                    roleRepository.save(newRole);
//                    roles.add(newRole);
//                }
//            }
//            user.setRoleSet(roles);
//        }
//
//        User savedUser = userRepository.save(user);
//        return userMapper.toDto(savedUser, "User");
    //-----------------------------------------------------------------

//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        Set<Role> roles = user.getRoleSet().stream()
//                .map(role -> roleRepository.findByRoleName(role.getRoleName())
//                        .orElseThrow(() -> new IllegalArgumentException("Role not found : " + role.getRoleName())))
//                .collect(Collectors.toSet());
//        user.setRoleSet(roles);
//        user.setCredentialsExpiryDate(LocalDateTime.now().plusMonths(6));
//        User savedUser = userRepository.save(user);
//        return userMapper.toDto(savedUser, "User");
//    }


    @Transactional
    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

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
    public List<UserDto> saveAll(List<UserDto> userDtos) {
        userDtos.forEach(userDto -> {
            UserDto savedUser = save(userDto);
        });
        return userDtos;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> userMapper.toDto(u))
                .collect(Collectors.toList());
    }

    public UserDto findById(UUID id) {
        User user = userRepository.findByUserUuid(id).orElse(null);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto update(UUID id, UserDto userDto) {
        User user = userRepository.findByUserUuid(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateFromDto(userDto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void logicalRemove(UUID id) {

        User user = userRepository.findByUserUuid(id).orElse(null);
        userRepository.logicalRemove(user.getId());
    }
}
