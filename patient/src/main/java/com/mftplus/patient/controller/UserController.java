package com.mftplus.patient.controller;


import com.mftplus.patient.dto.UserDto;
import com.mftplus.patient.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PutMapping("/edit/{id}")
    public UserDto updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        return userService.findById(id);
    }


    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }


    @GetMapping("/username/{username}/pass/{password}")
    public ResponseEntity<UserDto> findByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
        return ResponseEntity.ok(userService.findByUsernameAndPassword(username, password));
    }

    @GetMapping("/exist/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.existsByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.valueOf(404)).build();
        }
    }

    @GetMapping("/mail/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/userRole/{roleName}")
    public ResponseEntity<Set<UserDto>> findByRoles(@PathVariable String roleName) {
        return ResponseEntity.ok(userService.findByRoles(roleName));
    }

    @GetMapping("/userPerm/{permissionName}")
    public ResponseEntity<Set<UserDto>> findBPermsOfUser(@PathVariable String permissionName) {
        return ResponseEntity.ok(userService.findBPermsOfUser(permissionName));
    }


    @DeleteMapping("/remove/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.logicalRemove(id);
    }

}