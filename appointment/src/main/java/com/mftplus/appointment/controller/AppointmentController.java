package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.service.AppointmentService;
import com.mftplus.appointment.service.ScheduleService;
import com.mftplus.appointment.service.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final SpecializationService specializationService;
    private final ScheduleService scheduleService;

    public AppointmentController(AppointmentService appointmentService, SpecializationService specializationService, ScheduleService scheduleService) {
        this.appointmentService = appointmentService;
        this.specializationService = specializationService;
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> create(@RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.create(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<AppointmentDto> update(@PathVariable UUID id, @RequestBody AppointmentDto dto) {
        AppointmentDto updated = appointmentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAll() {
        List<AppointmentDto> appointments = appointmentService.getAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getByPatientId(@PathVariable UUID patientId) {
        return ResponseEntity.ok(appointmentService.getByPatientId(patientId));
    }


    //For Make Short Path / Client cans see selected specialization on /appointments/*
    @GetMapping("/specializations/{specializationId}")
    public ResponseEntity<List<ScheduleDto>> findAvailableSchedulesBySpecialization(@PathVariable UUID specializationId) {
        List<ScheduleDto> scheduleDtos = scheduleService.findAvailableSchedulesBySpecialization(specializationId);
        return ResponseEntity.ok(scheduleDtos);

    }

    //For Make Short Path / Admin of appointment(PERM_ACCESS_ALL_SPEC) can see All specialization on /appointments/*
    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpec() {
        List<SpecializationDto> specializationDtos = specializationService.findAll();
        return ResponseEntity.ok().body(specializationDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.logicalRemove(id);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
