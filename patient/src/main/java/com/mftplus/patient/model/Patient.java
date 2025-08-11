package com.mftplus.patient.model;

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
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "patientId")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientId")
    private Long patientId;

    @Column(name = "patient_uuid", unique = true, updatable = false)
    private UUID patientUuid;

    @Column(name = "appointment_uuid", unique = true,updatable = false)
    private UUID appointmentId;

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

    @Column(name = "deleted")
    private boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "user-id")
    private User user;

    @PrePersist
    public void generateUUID() {
        if (patientUuid == null) {
            patientUuid = UUID.randomUUID();
        }
    }

}
