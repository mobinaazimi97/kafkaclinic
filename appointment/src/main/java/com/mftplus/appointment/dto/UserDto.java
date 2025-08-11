package com.mftplus.appointment.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserDto {
    private UUID userUuid;
    private String username;
    private String password;
    private String email;
    private Set<RoleDto> roles = new HashSet<>();
}
