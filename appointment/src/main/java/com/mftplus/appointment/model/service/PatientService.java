package com.mftplus.appointment.model.service;

import com.mftplus.appointment.dto.AppointmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "patientService", url = "https://localhost:9443")
public interface PatientService {
//    @GetMapping("/patients/{patientId}")
//    ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable("patientId") UUID patientId);

    @GetMapping("/patients/{patientId}")
    ResponseEntity<AppointmentDto> getAppointmentsByPatientId(@PathVariable("patientId") UUID patientId);
}
