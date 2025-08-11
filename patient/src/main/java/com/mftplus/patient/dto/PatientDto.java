package com.mftplus.patient.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDto {
    private UUID patientUuid;
    private UUID appointmentId;
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
}
