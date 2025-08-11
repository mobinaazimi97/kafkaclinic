package com.mftplus.appointment.repository;

import com.mftplus.appointment.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByUserUuid(UUID uerUuid);

    @Modifying
    @Query("update userEntity u set u.deleted=true where u.id=:id")
    @Transactional
    void logicalRemove(Long id);
}
