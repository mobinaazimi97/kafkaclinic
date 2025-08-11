package com.mftplus.patient.service;

import com.mftplus.patient.dto.AppointmentDto;
import com.mftplus.patient.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "appointmentService", url = "https://localhost:8443")
public interface AppointmentService {

    @PostMapping("/appointments")
    ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto);

    @GetMapping("/appointments/{appointmentId}")
    ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable("appointmentId") UUID appointmentId);

    @GetMapping("/appointments/patients/{patientId}")
    ResponseEntity<List<?>> getAppointmentsByPatientId(@PathVariable("patientId") UUID patientId);

    @GetMapping("/appointments/specializations/{specializationId}")
    ResponseEntity<List<?>> findAvailableSchedulesBySpecializationInAppointment(@PathVariable UUID specializationId);

    @GetMapping("/appointments/specializations")
    ResponseEntity<List<?>> getAllSpec();

//    @PostMapping("/appointments")
//    ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto);
//
//    @GetMapping("/appointments")
//    ResponseEntity<List<AppointmentDto>> getAppointments();
//
//    @GetMapping("/patients/appointments/{appointmentId}")
//    ResponseEntity<List<PatientDto>> getAppointmentById(@PathVariable("appointmentId") Long appointmentId);

//    @GetMapping("/appointments/patients/{patientId}")
//    ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable("patientId") Long patientId);

}
