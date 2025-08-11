package com.mftplus.patient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "roleEntity")
@Table(name = "roles_patients")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_uuid", unique = true, updatable = false)
    private UUID roleUuid;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Version
    @JsonIgnore
    private Long versionId;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @PrePersist
    public void generateUUID() {
        if (roleUuid == null) {
            roleUuid = UUID.randomUUID();
        }
    }
}