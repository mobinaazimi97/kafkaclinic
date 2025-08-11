package com.mftplus.appointment.controller;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public List<PermissionDto> getPermissions() {
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public PermissionDto getPermission(@PathVariable UUID id) {
        return permissionService.findById(id);
    }

    @PostMapping
    public PermissionDto createPermission(@RequestBody PermissionDto permissionDto) {
        return permissionService.save(permissionDto);
    }

    @PutMapping("/edit/{id}")
    public PermissionDto updatePermission(@PathVariable UUID id, @RequestBody PermissionDto permissionDto) {
        return permissionService.update(id, permissionDto);
    }

    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable UUID id) {
        permissionService.logicalRemove(id);
    }

}
