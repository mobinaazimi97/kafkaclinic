package com.mftplus.appointment.dto;

import lombok.ToString;
import java.util.UUID;

//@Getter
//@Setter
//@NoArgsConstructor
//@SuperBuilder
@ToString
public class SpecializationDto {
    private UUID specializationUuid;
    private String skillName;
    private String description;
    private boolean deleted = false;

    private UUID doctorId;
    private String doctorFirstName;
    private String doctorLastName;

    public SpecializationDto() {
    }

    public SpecializationDto(UUID specializationUuid, String skillName, String description, boolean deleted, UUID doctorId, String doctorFirstName, String doctorLastName) {
        this.specializationUuid = specializationUuid;
        this.skillName = skillName;
        this.description = description;
        this.deleted = deleted;
        this.doctorId = doctorId;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
    }

    public SpecializationDto(String skillName, String description, boolean deleted, UUID doctorId, String doctorFirstName, String doctorLastName) {
        this.skillName = skillName;
        this.description = description;
        this.deleted = deleted;
        this.doctorId = doctorId;
        this.doctorFirstName = doctorFirstName;
        this.doctorLastName = doctorLastName;
    }

    public UUID getSpecializationUuid() {
        return specializationUuid;
    }

    public SpecializationDto setSpecializationUuid(UUID specializationUuid) {
        this.specializationUuid = specializationUuid;
        return this;
    }

    public String getSkillName() {
        return skillName;
    }

    public SpecializationDto setSkillName(String skillName) {
        this.skillName = skillName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SpecializationDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public SpecializationDto setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public SpecializationDto setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    public SpecializationDto setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
        return this;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public SpecializationDto setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
        return this;
    }
}
