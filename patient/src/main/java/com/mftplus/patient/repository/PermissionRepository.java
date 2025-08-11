package com.mftplus.patient.repository;

import com.mftplus.patient.model.Permission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String name);

    Optional<Permission> findByPermUuid(UUID permUuid);


    @Modifying
    @Query("update permissionEntity p set p.deleted=true where p.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
