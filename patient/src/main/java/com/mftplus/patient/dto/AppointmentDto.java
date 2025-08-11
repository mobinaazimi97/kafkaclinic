package com.mftplus.patient.dto;

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
//@ToString
public class AppointmentDto {
    private UUID id;
    private UUID patientId;
    private LocalDateTime appointmentDateTime;
    private String notes;
    private UUID scheduleId;

    private String patientFirstName;
    private String patientLastName;
    private String patientPhone;
}
