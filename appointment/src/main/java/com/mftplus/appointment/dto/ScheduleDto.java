package com.mftplus.appointment.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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
@ToString
@SuperBuilder
public class ScheduleDto {
    private UUID scheduleUuid;
    private UUID doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int appointmentDurationMin;

    private UUID id;
    private LocalDateTime appointmentDateTime;
    private UUID patientId;

    private boolean deleted = false;


    @JsonProperty("isBooked")
    private Boolean isBooked;

}
