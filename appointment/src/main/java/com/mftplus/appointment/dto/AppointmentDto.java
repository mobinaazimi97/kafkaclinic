package com.mftplus.appointment.dto;


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
public class AppointmentDto {
    private UUID appointmentUuid;
    private UUID patientId;

    private UUID scheduleId;

    private LocalDateTime appointmentDateTime;
    private String notes;

    public String getPersianStartDate() {
        return PersianDate.fromGregorian(LocalDate.from(appointmentDateTime)).toString();

    }

    public void setPersianStartDate(String persianStartDate) {
        this.appointmentDateTime = LocalDateTime.from(PersianDate.parse(persianStartDate).toGregorian());
    }
}
