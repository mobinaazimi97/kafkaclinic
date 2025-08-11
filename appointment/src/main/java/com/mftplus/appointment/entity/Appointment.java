package com.mftplus.appointment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Cacheable
@Entity(name = "appointmentEntity")
@Table(name = "appointments")
@ToString(exclude = "schedule")
@SQLRestriction("deleted = false")
public class Appointment extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_uuid", unique = true, nullable = false, updatable = false)
    private UUID appointmentUuid;

    @Column(name = "patient_uuid", unique = true)
    private UUID patientId;

    @ManyToOne
    @JsonBackReference(value = "schedule-appointments")
    @JoinColumn(name = "schedule_id", foreignKey = @ForeignKey(name = "fk_schedule"))
    private Schedule schedule;

    @Column(name = "appointment_At")
    private LocalDateTime appointmentDateTime;

    @Column(name = "notes")
    private String notes;

    @OneToOne
    @JoinColumn(name = "user-id")
    private User user;


    @PrePersist
    public void generateUUID() {
        if (appointmentUuid == null) {
            appointmentUuid = UUID.randomUUID();
        }
    }
}
