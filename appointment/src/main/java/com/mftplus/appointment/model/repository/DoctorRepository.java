package com.mftplus.appointment.model.repository;

import com.mftplus.appointment.model.entity.Doctor;
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
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("select d from doctorEntity d where d.doctorFirstname=:doctorFirstname and d.doctorLastname=:doctorLastname")
    List<Doctor> findByDoctorName(@Param("doctorFirstname") String doctorFirstname, @Param("doctorLastname") String doctorLastname);


    @Query("select distinct d from doctorEntity d " +
            "join fetch d.schedules s " +
            "where s.scheduleId = :scheduleId")
    Optional<Doctor> findSchedule(@Param("scheduleId") Long scheduleId);

    @Query("select distinct d from doctorEntity d join fetch d.schedules s where d.doctorId = :doctorId and s.isBooked = false")
    Optional<Doctor> findDoctorWithAvailableSchedules(@Param("doctorId") Long doctorId);



    @Query("select d from doctorEntity d cross join specializationEntity sp where sp.deleted=false")
    List<Doctor> findSpecializations();

    @Query("select d from doctorEntity d left join fetch d.specializations where d.doctorId = :doctorId and d.deleted = false")
    Optional<Doctor> findDoctorWithSpecializationsByDoctorId(@Param("doctorId") Long doctorId);


    @Query("select d from doctorEntity d where d.doctorUuid=:doctorUuid")
    Optional<Doctor>findByDoctorUuid(@Param("doctorUuid") UUID doctorUuid);

    @Modifying
    @Query("update doctorEntity d set d.deleted=true where d.doctorId= :doctorId")
    @Transactional
    void logicalRemove(@Param("doctorId") Long doctorId);
}
