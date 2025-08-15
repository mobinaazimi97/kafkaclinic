package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.Appointment;
import com.mftplus.appointment.model.entity.Schedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUuid(UUID patientUuid);

    Optional<Appointment> findByAppointmentUuid(UUID appointmentUuid);

    boolean existsByScheduleAndAppointmentDateTime(Schedule schedule, LocalDateTime appointmentDateTime);

    @Modifying
    @Query("update appointmentEntity a set a.deleted=true where a.id=:id")
    @Transactional
    void logicalRemove(Long id);

}
