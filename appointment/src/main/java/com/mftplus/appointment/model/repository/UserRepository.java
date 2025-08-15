package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.User;
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
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByUserUuid(UUID uerUuid);

    @Query("select u from userEntity u where u.username like :username and u.password=:password")
    Optional<User> findByUsernameAndPassword(String username, String password);
    User findByEmail(String email);

    @Query("select distinct u from userEntity u " +
            "join fetch u.roles r " +
            "where r.roleName like :roleName")
    Set<User> findByRoles(@Param("roleName") String roleName);


    @Query("select distinct u from userEntity u " +
            "join fetch u.roles r " +
            "join fetch r.permissions p " +
            "where p.permissionName like :permissionName")
    Set<User> findBPermsOfUser(@Param("permissionName") String permissionName);

    @Modifying
    @Query("update userEntity u set u.deleted=true where u.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
