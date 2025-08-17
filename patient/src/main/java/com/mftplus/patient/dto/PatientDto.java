package com.mftplus.patient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDto {
    //Appointment-data
    private UUID appointmentUuid;
    private UUID scheduleId;
    //-------------------------------------------------------------
    private UUID patientUuid;
    private LocalDateTime appointmentDateTime;
    private String notes;
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String patientNumber;
    private UUID userUuid;
    private String username;


}
