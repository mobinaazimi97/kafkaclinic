package com.mftplus.patient.controller;

import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.model.service.AppointmentService;
import com.mftplus.patient.model.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/patients")
@Slf4j
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }


    @PostMapping("/appointments/save")
    public ResponseEntity<PatientDto> createAppointments(@RequestBody PatientDto createAppointment) {

        PatientDto savedAppointment = patientService.savePatient(createAppointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @PutMapping("/edit/{patientId}")
    //No Use - in url for uuid !!
    public ResponseEntity<PatientDto> updatePatient(@PathVariable UUID patientId, @RequestBody PatientDto patientDto) {
        PatientDto updatedPatient = patientService.updatePatient(patientId, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable UUID patientId) {
        PatientDto dto = patientService.findById(patientId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/myAppointments/{patientId}")
    public ResponseEntity<PatientDto> getMyAppointments(@PathVariable UUID patientId) {
        PatientDto dto = patientService.findById(patientId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{firstName}/{lastName}")
    public ResponseEntity<PatientDto> findByFullName(@PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok(patientService.findByFullName(firstName, lastName));
    }


    @DeleteMapping("/remove/{patientId}")
    //No Use - in url for uuid !!
    public ResponseEntity<Void> deletePatient(@PathVariable UUID patientId) {
        patientService.logicalRemove(patientId);
        return ResponseEntity.noContent().build();
    }

    // APPOINTMENT ACTIONS :
    //For Admins
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<List<PatientDto>> getPatientsByAppointmentId(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(patientService.getByAppointmentId(appointmentId));
    }

    //Appointments Of This Patient*
    @GetMapping("/appointments/patients/{patientId}")
    public ResponseEntity<List<?>> getPatientsByPatientId(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId).getBody());
    }

    //Patient can see all specializations with free times of them :
    @GetMapping("/appointments/specializations")
    public ResponseEntity<List<?>> getAllSpec() {

        return ResponseEntity.ok(appointmentService.getAllSpec().getBody());
    }

    //Patient can see and select special specialization :
    @GetMapping("/appointments/specializations/{specializationId}")
    public ResponseEntity<List<?>> findAvailableSchedulesBySpecializationInAppointment(@PathVariable UUID specializationId) {
        return ResponseEntity.ok(appointmentService.findAvailableSchedulesBySpecializationInAppointment(specializationId).getBody());
    }

}
