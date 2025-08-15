package com.mftplus.patient.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder

@Entity(name = "permissionEntity")
@Table(name = "permissions_patients")
@SQLRestriction("deleted = false")
@Cacheable
public class Permission extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_uuid", unique = true, updatable = false)
    private UUID permUuid;

    @Column(name = "permission_name", unique = true, nullable = false)
    private String permissionName;

    @PrePersist
    public void generateUUID() {
        if (permUuid == null) {
            permUuid = UUID.randomUUID();
        }
    }
}
