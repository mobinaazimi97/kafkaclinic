package com.mftplus.patient.repository;

import com.mftplus.patient.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String name);

    Optional<Role> findByRoleUuid(UUID uuid);

    @Modifying
    @Query("update roleEntity r set r.deleted=true where r.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
