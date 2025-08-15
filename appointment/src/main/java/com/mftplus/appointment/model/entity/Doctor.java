package com.mftplus.appointment.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"specializations", "schedules"})
@SuperBuilder
@Entity(name = "doctorEntity")
@Table(name = "doctors")
@SQLRestriction("deleted = false")
@Cacheable
public class Doctor extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(name = "doctor_uuid", unique = true, updatable = false)
    private UUID doctorUuid;

    @Column(name = "doctor_Firstname")
    private String doctorFirstname;

    @Column(name = "doctor_Lastname")
    private String doctorLastname;

    @Column(name = "experience_years")
    private int experienceYears;

    @Column(name = "contact_info")
    private String contactInfo;

    @ManyToMany
    @JsonManagedReference(value = "doctor-specialization")
    @JoinTable(
            name = "spec_doctor",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Specialization> specializations = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference(value = "doctor-schedule")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Schedule> schedules = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (doctorUuid == null) {
            doctorUuid = UUID.randomUUID();
        }
    }
}