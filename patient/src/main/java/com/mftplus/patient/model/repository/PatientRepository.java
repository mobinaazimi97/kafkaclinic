package com.mftplus.patient.model.repository;

import com.mftplus.patient.model.entity.Patient;
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
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("select p from patientEntity p where p.firstName like :firstName and p.lastName like :lastName")
    Optional<Patient> findByFullName(String firstName , String lastName);

    List<Patient> findByAppointmentUuid(UUID appointmentUuid);

    Optional<Patient>findByPatientUuid(UUID patientUuid);

    @Modifying
    @Query("update patientEntity p set p.deleted=true where p.patientId= :patientId")
    @Transactional
    void logicalRemove(@Param("patientId") Long patientId);

    boolean existsByPatientUuid(UUID patientId);
}
