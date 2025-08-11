package com.mftplus.patient.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class PermissionDto {

    private UUID permUuid;
    private String permissionName;
}
