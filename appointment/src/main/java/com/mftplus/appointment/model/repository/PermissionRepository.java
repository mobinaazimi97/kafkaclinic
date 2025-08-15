package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.Permission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("select p from permissionEntity p where p.permissionName like :permissionName")
    Optional<Permission> findByPermissionName(String permissionName);

    Optional<Permission> findByPermUuid(UUID permUuid);


    @Modifying
    @Query("update permissionEntity p set p.deleted=true where p.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
