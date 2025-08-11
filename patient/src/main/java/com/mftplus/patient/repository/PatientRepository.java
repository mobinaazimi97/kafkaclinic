package com.mftplus.patient.repository;

import com.mftplus.patient.model.Patient;
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

    Optional<Patient> findByLastName(String lastName);

    List<Patient> findByAppointmentId(UUID appointmentId);

    Optional<Patient>findByPatientUuid(UUID patientUuid);

    @Modifying
    @Query("update patientEntity p set p.deleted=true where p.patientId= :patientId")
    @Transactional
    void logicalRemove(@Param("patientId") Long patientId);

}
