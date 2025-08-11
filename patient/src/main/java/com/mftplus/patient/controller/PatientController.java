package com.mftplus.patient.controller;

import com.mftplus.patient.dto.AppointmentDto;
import com.mftplus.patient.dto.PatientDto;
import com.mftplus.patient.service.AppointmentService;
import com.mftplus.patient.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    //TODO:
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        PatientDto savedPatient = patientService.savePatient(patientDto);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/edit/{patientId}")
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

    //--------------------------------------------------------------------------------------------------
// APPOINTMENT ACTIONS :
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<List<PatientDto>> getPatientsByAppointmentId(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(patientService.getByAppointmentId(appointmentId));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDto> createAppointments(@RequestBody AppointmentDto appointmentDto) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentDto).getBody());

    }

    //Checked+
    //Appointments Of This Patient*
    @GetMapping("/appointments/patients/{patientId}")
    public ResponseEntity<List<?>> getPatientsByPatientId(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId).getBody());
    }

    @GetMapping("/appointments/specializations/{specializationId}")
    public ResponseEntity<List<?>> findAvailableSchedulesBySpecializationInAppointment(@PathVariable UUID specializationId) {
        return ResponseEntity.ok(appointmentService.findAvailableSchedulesBySpecializationInAppointment(specializationId).getBody());
    }

    @GetMapping("/appointments/specializations")
    public ResponseEntity<List<?>> getAllSpec() {
        return ResponseEntity.ok(appointmentService.getAllSpec().getBody());
    }

    //--------------------------------------------------------------------------------------------------
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID patientId) {
        patientService.logicalRemove(patientId);
        return ResponseEntity.noContent().build();
    }
//    @ExceptionHandler(ScheduleAlreadyBookedException.class)
//    public ResponseEntity<AppointmentResponse> handleScheduleAlreadyBookedException(ScheduleAlreadyBookedException ex) {
//        AppointmentResponse response = new AppointmentResponse(false, "این برنامه قبلاً رزرو شده است", null);
//        return ResponseEntity.ok(response);
//    }
//
//    @ExceptionHandler(AppointmentTimeTakenException.class)
//    public ResponseEntity<AppointmentResponse> handleAppointmentTimeTakenException(AppointmentTimeTakenException ex) {
//        AppointmentResponse response = new AppointmentResponse(false, "این زمان قبلاً رزرو شده است", null);
//        return ResponseEntity.ok(response);
//    }
//
//    @ExceptionHandler(ScheduleNotFoundException.class)
//    public ResponseEntity<AppointmentResponse> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
//        AppointmentResponse response = new AppointmentResponse(false, ex.getMessage(), null);
//        return ResponseEntity.ok(response);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<AppointmentResponse> handleGeneralException(Exception ex) {
//        AppointmentResponse response = new AppointmentResponse(false, "خطا در پردازش درخواست: " + ex.getMessage(), null);
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping
//    public ResponseEntity<PatientDto> savePatient(@RequestBody PatientDto dto) {
//        PatientDto saved = patientService.savePatient(dto);
//        return ResponseEntity.ok(saved);
//    }

//    @PostMapping("/patients")
//    public ResponseEntity<?> createPatient(@RequestBody Patient patient) throws JsonProcessingException {
//        // save to DB ...
//        patientRepository.save(patient);
//        String patientJson = new ObjectMapper().writeValueAsString(patient);
//        patientKafkaProducer.sendPatientData(patientJson);
//        return ResponseEntity.ok("Saved and sent to Kafka.");
//    }
//
//    @PostMapping("/withAppointment")
//    public ResponseEntity<PatientDto> savePatientAndRequestAppointment(@RequestBody PatientDto dto) {
////        PatientDto saved = patientService.savePatientAndRequestAppointment(dto);
//        PatientDto saved = patientService.savePatient(dto);
//        return ResponseEntity.ok(saved);
//    }
//
//    @PutMapping("/{patientId}")
//    public ResponseEntity<PatientDto> update(@PathVariable Long patientId, @RequestBody PatientDto dto) {
//        PatientDto updated = patientService.updatePatient(patientId, dto);
//        return ResponseEntity.ok(updated);
//    }
//
//
//
//    @GetMapping
//    public ResponseEntity<List<PatientDto>> getAllPatients() {
//        List<PatientDto> patients = patientService.findAll();
//        return ResponseEntity.ok(patients);
//    }
//
//
//    @DeleteMapping("/{patientId}")
//    public ResponseEntity<?> deletePatient(@PathVariable Long patientId) {
//        patientService.logicalRemove(patientId);
//        return ResponseEntity.ok("Patient with ID " + patientId + " has been successfully deleted.");
//    }

}
