package com.mftplus.patient.controller;



import com.mftplus.patient.dto.RoleDto;
import com.mftplus.patient.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable UUID id) {
        return roleService.findById(id);
    }

    @PostMapping
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        return roleService.save(roleDto);
    }

    @PutMapping("/edit/{id}")
    public RoleDto updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto) {
        return roleService.update(id, roleDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.logicalRemove(id);
    }

}
