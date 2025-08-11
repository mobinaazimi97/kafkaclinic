package com.mftplus.appointment.repository;

import com.mftplus.appointment.entity.Specialization;
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
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    @Query("select s from specializationEntity s where s.skillName=:skillName")
    List<Specialization> findBySkillName(@Param("skillName") String skillName);

    @Query("select s from specializationEntity s cross join doctorEntity d where d.doctorFirstname=:doctorFirstname and d.doctorLastname=:doctorLastname")
    List<Specialization> findByDoctorName(@Param("doctorFirstname") String doctorFirstname, @Param("doctorLastname") String doctorLastname);

    @Modifying
    @Query("update specializationEntity s set s.deleted=true where s.specializationId= :specializationId")
    @Transactional
    void logicalRemove(@Param("specializationId") Long specializationId);

    @Query("select sp from specializationEntity sp where sp.specializationUuid=:specializationUuid")
    Optional<Specialization> findByUuId(UUID specializationUuid);
}
