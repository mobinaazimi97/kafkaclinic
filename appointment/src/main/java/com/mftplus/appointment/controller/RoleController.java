package com.mftplus.appointment.controller;


import com.mftplus.appointment.dto.RoleDto;
import com.mftplus.appointment.model.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleService.save(roleDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleService.update(id, roleDto));
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.findById(id));
    }


    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleDto> findByRoleName(@PathVariable String roleName) {
        return ResponseEntity.ok(roleService.findByRoleName(roleName));
    }

    @GetMapping("/perm/{permissionName}")
    public ResponseEntity<Set<RoleDto>> findByPermissionName(@PathVariable String permissionName) {
        return ResponseEntity.ok(roleService.findByPermissionName(permissionName));
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        roleService.logicalRemove(id);
        return ResponseEntity.ok("Role Removed With ID " + id + " has been successfully deleted.");
    }
}
