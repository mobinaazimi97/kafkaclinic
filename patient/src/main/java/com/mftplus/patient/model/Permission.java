package com.mftplus.patient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

@Entity(name = "permissionEntity")
@Table(name = "permissions_patients")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_uuid", unique = true, updatable = false)
    private UUID permUuid;

    @Column(name = "permission_name", unique = true, nullable = false)
    private String permissionName;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Version
    @JsonIgnore
    private Long versionId;

    @PrePersist
    public void generateUUID() {
        if (permUuid == null) {
            permUuid = UUID.randomUUID();
        }
    }
}
