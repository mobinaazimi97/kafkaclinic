package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from roleEntity r where r.roleName like :roleName")
    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findByRoleUuid(UUID uuid);

    @Query("select distinct r from roleEntity r " +
            "join fetch r.permissions p " +
            "where p.permissionName like :permissionName")
    Set<Role> findByPermissions(@Param("permissionName") String permissionName);


    @Modifying
    @Query("update roleEntity r set r.deleted=true where r.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
