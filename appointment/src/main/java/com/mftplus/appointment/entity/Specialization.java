package com.mftplus.appointment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@ToString(exclude = "doctors")
@SuperBuilder
@Entity(name = "specializationEntity")
@Table(name = "specializations")
@SQLRestriction("deleted = false")
@Cacheable
public class Specialization extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long specializationId;

    @Column(name = "specialization_uuid", unique = true, nullable = false, updatable = false)
    private UUID specializationUuid;


    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "description")
    private String description;


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference(value = "doctor-specialization")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Doctor> doctors = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (specializationUuid == null) {
            specializationUuid = UUID.randomUUID();
        }
    }
}
