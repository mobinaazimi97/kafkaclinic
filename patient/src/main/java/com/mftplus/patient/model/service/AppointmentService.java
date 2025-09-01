package com.mftplus.patient.model.service;

import com.mftplus.patient.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "appointmentService", url = "https://localhost:8443"
)
public interface AppointmentService {

    @PostMapping("/appointments/save")
    ResponseEntity<PatientDto> createAppointment(@RequestBody PatientDto appointmentDto);

    @GetMapping("/appointments/{appointmentId}")
    ResponseEntity<PatientDto> getAppointmentById(@PathVariable("appointmentId") UUID appointmentId);

    @GetMapping("/appointments/patients/{patientId}")
    ResponseEntity<?> getAppointmentsByPatientId(@PathVariable("patientId") UUID patientId);

    @GetMapping("/appointments/specializations/{specializationId}")
    ResponseEntity<List<?>> findAvailableSchedulesBySpecializationInAppointment(@PathVariable("specializationId") UUID specializationId);

    @GetMapping("/appointments/specializations")
    ResponseEntity<List<?>> getAllSpec();
}
