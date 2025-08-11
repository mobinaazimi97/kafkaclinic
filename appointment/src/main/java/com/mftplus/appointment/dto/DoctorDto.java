package com.mftplus.appointment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(exclude = {"specializations", "schedules"})
public class DoctorDto {
    private UUID doctorUuid;
    private String doctorFirstname;
    private String doctorLastname;
    private String contactInfo;
    private boolean deleted = false;
    private int experienceYears;

    private List<SpecializationDto> specializations = new ArrayList<>();

    private List<ScheduleDto> schedules = new ArrayList<>();

}
