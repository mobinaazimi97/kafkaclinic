package com.mftplus.appointment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity(name = "scheduleEntity")
@Table(name = "schedules")
@SQLRestriction("deleted = false")
@Cacheable
public class Schedule extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(name = "schedule_uuid", unique = true, nullable = false, updatable = false)
    private UUID scheduleUuid;

    @Column(name = "start_At")
    private LocalDateTime startDateTime;

    @Column(name = "end_At")
    private LocalDateTime endDateTime;

    @Column(name = "appointment_Length")
    private int appointmentDurationMin;

    @Column(name = "isBooked")
    @JsonProperty("isBooked")
    private Boolean isBooked;

    @OneToMany(mappedBy = "schedule")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "fk_doctor"))
    private Doctor doctor;

    public boolean isBooked() {
        return isBooked;
    }

    public Schedule setBooked(boolean booked) {
        isBooked = booked;
        return this;
    }

    @PrePersist
    public void generateUUID() {
        if (scheduleUuid == null) {
            scheduleUuid = UUID.randomUUID();
        }
    }
}