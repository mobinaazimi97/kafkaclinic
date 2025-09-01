package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.model.service.AppointmentService;
import com.mftplus.appointment.model.service.ScheduleService;
import com.mftplus.appointment.model.service.SpecializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@Slf4j
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final SpecializationService specializationService;
    private final ScheduleService scheduleService;

    public AppointmentController(AppointmentService appointmentService, SpecializationService specializationService, ScheduleService scheduleService) {
        this.appointmentService = appointmentService;
        this.specializationService = specializationService;
        this.scheduleService = scheduleService;
    }


    @PostMapping("/save")
    public ResponseEntity<AppointmentDto> create(@RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.create(dto));
    }

    @PutMapping("/edit/{id}")
    //No Use - in url for uuid !!
    public ResponseEntity<AppointmentDto> update(@PathVariable UUID id, @RequestBody AppointmentDto dto) {
        AppointmentDto updated = appointmentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAll() {
        List<AppointmentDto> appointments = appointmentService.getAll();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getById(@PathVariable("appointmentId") UUID appointmentId) {
        return ResponseEntity.ok(appointmentService.getById(appointmentId));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<?> getByPatientId(@PathVariable("patientId") UUID patientId) {
        return ResponseEntity.ok(appointmentService.getByPatientId(patientId));
    }


    //For Make Short Path / Client cans see selected specialization on /appointments/*
    @GetMapping("/specializations/{specializationId}")
    public ResponseEntity<List<ScheduleDto>> findAvailableSchedulesBySpecialization(@PathVariable("specializationId") UUID specializationId) {
        List<ScheduleDto> scheduleDtos = scheduleService.findAvailableSchedulesBySpecialization(specializationId);
        return ResponseEntity.ok(scheduleDtos);

    }

    //For Make Short Path / Admin of appointment(PERM_ACCESS_ALL_SPEC) can see All specialization on /appointments/*
    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpec() {
        List<SpecializationDto> specializationDtos = specializationService.findAll();
        return ResponseEntity.ok().body(specializationDtos);
    }

    @DeleteMapping("/remove/{id}")
    //No Use - in url for uuid !!
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.logicalRemove(id);
        return ResponseEntity.noContent().build();
    }
}
