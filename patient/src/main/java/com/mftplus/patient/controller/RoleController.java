package com.mftplus.patient.controller;


import com.mftplus.patient.dto.RoleDto;
import com.mftplus.patient.model.service.RoleService;
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
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        return roleService.save(roleDto);
    }

    @PutMapping("/edit/{id}")
    public RoleDto updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto) {
        return roleService.update(id, roleDto);
    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable UUID id) {
        return roleService.findById(id);
    }


    @GetMapping("/roleName/{name}")
    public ResponseEntity<RoleDto> findByRoleName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.findByRoleName(name));
    }

    @GetMapping("/perm/{permission}")
    public ResponseEntity<Set<RoleDto>> findByPermissions(@PathVariable String permission) {
        return ResponseEntity.ok(roleService.findByPermissions(permission));
    }

    @DeleteMapping("/remove/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.logicalRemove(id);
    }

}
