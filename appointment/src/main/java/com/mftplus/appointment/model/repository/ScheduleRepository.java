package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM scheduleEntity s " +
            "JOIN s.doctor d " +
            "JOIN d.specializations sp " +
            "WHERE sp.specializationId = :specializationId " +
            "AND s.isBooked = false " +
            "AND s.startDateTime >= CURRENT_TIMESTAMP " +
            "ORDER BY s.startDateTime")
    List<Schedule> findAvailableSchedulesBySpecialization(@Param("specializationId") Long specializationId);

    @Modifying
    @Query("update scheduleEntity s set s.deleted=true where s.scheduleId= :scheduleId")
    @Transactional
    void logicalRemove(@Param("scheduleId") Long scheduleId);


    @Query("select s from scheduleEntity s where s.scheduleUuid = :scheduleUuid")
    Optional<Schedule> findByScheduleUuid(UUID scheduleUuid);

}
