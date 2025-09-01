package com.mftplus.appointment.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.mfathi91.time.PersianDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDto {

    private UUID appointmentUuid;
    private UUID patientUuid;
    private LocalDateTime appointmentDateTime;
    private String notes;
    private UUID scheduleId;


    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String patientNumber;
    private UUID userUuid;
    private String username;

    private String doctorFirstname;
    private String doctorLastname;


    public String getPersianStartDate() {
        if (appointmentDateTime == null) {
            return null; // یا "" بسته به نیاز
        }
        return PersianDate.fromGregorian(LocalDate.from(appointmentDateTime)).toString();
    }

    public void setPersianStartDate(String persianStartDate) {
        if (persianStartDate == null || persianStartDate.isEmpty()) {
            this.appointmentDateTime = null;
            return;
        }
        this.appointmentDateTime = LocalDateTime.from(
                PersianDate.parse(persianStartDate).toGregorian()
        );
    }

}