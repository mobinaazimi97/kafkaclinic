package com.mftplus.appointment.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "roleEntity")
@Table(name = "roles")
@SQLRestriction("deleted = false")
@Cacheable
public class Role extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_uuid", unique = true, updatable = false)
    private UUID roleUuid;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
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