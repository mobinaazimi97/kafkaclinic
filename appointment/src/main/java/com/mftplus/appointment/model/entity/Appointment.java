package com.mftplus.appointment.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Column(name = "appointment_uuid", nullable = false, updatable = false)
    private UUID appointmentUuid;

    @Column(name = "patient_uuid")
    private UUID patientUuid;

    @Column(name = "patient_firstName")
    private String firstName;

    @Column(name = "patient_lastName")
    private String lastName;

    @Column(name = "patient_age")
    private int age;

    @Column(name = "patient_phone")
    private String phone;

    @Column(name = "patient_num")
    private String patientNumber;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;


    @PrePersist
    public void generateUUID() {
        if (appointmentUuid == null) {
            appointmentUuid = UUID.randomUUID();
        }
        if (patientUuid == null) {
            patientUuid = UUID.randomUUID();
      }else {
            setPatientUuid(patientUuid);
        }
    }
}
