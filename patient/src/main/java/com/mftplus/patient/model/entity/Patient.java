package com.mftplus.patient.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Cacheable
@Entity(name = "patientEntity")
@Table(name = "patients")
@SQLRestriction("deleted = false")
public class Patient extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientId")
    private Long patientId;

    @Column(name = "patient_uuid", unique = true, updatable = false)
    private UUID patientUuid;

    @Column(name = "appointment_uuid", unique = true, updatable = false)
    private UUID appointmentUuid;

    @Column(name = "schedule_uuid", unique = true, updatable = false)
    private UUID scheduleId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "phone")
    private String phone;

    @Column(name = "patientNumber")
    private String patientNumber;

    @Column(name = "notes", length = 80)
    private String notes;

    @OneToOne
    @JoinColumn(name = "user-id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    @PrePersist
    public void generateUUID() {
        if (patientUuid == null) {
            patientUuid = UUID.randomUUID();
        }
        if (appointmentUuid == null) {
            appointmentUuid = UUID.randomUUID();
        } else {
            setAppointmentUuid(appointmentUuid);
        }

    }

}
