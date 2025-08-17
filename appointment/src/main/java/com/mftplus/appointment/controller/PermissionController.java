package com.mftplus.appointment.controller;


import com.mftplus.appointment.dto.PermissionDto;
import com.mftplus.appointment.model.service.PermissionService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save")
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDto) {
        return ResponseEntity.ok(permissionService.save(permissionDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<PermissionDto> updatePermission(@PathVariable UUID id, @RequestBody PermissionDto permissionDto) {
        return ResponseEntity.ok(permissionService.update(id, permissionDto));
    }

    @GetMapping
    public ResponseEntity<List<PermissionDto>> getPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermission(@PathVariable UUID id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @GetMapping("/perm/{name}")
    public ResponseEntity<PermissionDto> findByPermissionName(@PathVariable String name) {
        return ResponseEntity.ok(permissionService.findByPermissionName(name));
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        permissionService.logicalRemove(id);
        return ResponseEntity.ok("Permission Removed With ID " + id + " has been successfully deleted.");
    }
}
