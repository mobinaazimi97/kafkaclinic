package com.mftplus.appointment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class SpecializationDto {
    private UUID specializationUuid;
    private String skillName;
    private String description;
    private boolean deleted = false;

    private UUID doctorId;
    private String doctorFirstName;
    private String doctorLastName;

}
