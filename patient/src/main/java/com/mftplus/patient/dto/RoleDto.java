package com.mftplus.patient.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class RoleDto {
    private UUID roleUuid;
    private String roleName;
    private Set<PermissionDto> permissions = new HashSet<>();
}
