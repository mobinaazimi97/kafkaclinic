package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.UserDto;
import com.mftplus.appointment.model.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adminSave")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.save(userDto));
    }

    @PostMapping("/save")
    @Secured({"ROLE_User", "ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> createUserByUser(@RequestBody UserDto userDto) {
        userDto.setUsername(userDto.getUsername());
        userDto.setPassword(userDto.getPassword());
        userDto.setEmail(userDto.getEmail());
        log.info("User Saved {}", userDto);
        UserDto result = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/edit/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public List<UserDto> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public UserDto getUser(@PathVariable UUID id) {
        return userService.findById(id);
    }


    @GetMapping("/username/{username}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }


    @GetMapping("/exist/{username}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.existsByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.valueOf(404)).build();
        }
    }

    @GetMapping("/username/{username}/pass/{password}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> findByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
        return ResponseEntity.ok(userService.findByUsernameAndPassword(username, password));
    }

    @GetMapping("/mail/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/userRole/{roleName}")
    public ResponseEntity<Set<UserDto>> findByRoles(@PathVariable String roleName) {
        return ResponseEntity.ok(userService.findByRoleName(roleName));
    }

    @GetMapping("/userPerm/{permissionName}")
    public ResponseEntity<Set<UserDto>> findBPermsOfUser(@PathVariable String permissionName) {
        return ResponseEntity.ok(userService.findBPermsOfUser(permissionName));
    }

    @DeleteMapping("/remove/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        userService.logicalRemove(id);
        return ResponseEntity.ok("User Removed With ID " + id + " has been successfully deleted.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}