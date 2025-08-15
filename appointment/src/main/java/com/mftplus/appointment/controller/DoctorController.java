package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.DoctorDto;
import com.mftplus.appointment.model.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/save")
    public ResponseEntity<DoctorDto> create(@RequestBody DoctorDto doctorDto) {
        DoctorDto saved = doctorService.save(doctorDto);
        return ResponseEntity.ok().body(saved);
    }

    @PutMapping("/edit/{doctorId}")
    public ResponseEntity<DoctorDto> update(@PathVariable UUID doctorId, @RequestBody DoctorDto doctorDto) {
        DoctorDto updated = doctorService.update(doctorId, doctorDto);
        return ResponseEntity.ok().body(updated);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctorDtoList = doctorService.findAll();
        return ResponseEntity.ok(doctorDtoList);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable UUID doctorId) {
        DoctorDto dto = doctorService.findById(doctorId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/{firstname}/{lastname}")
    public ResponseEntity<List<DoctorDto>> findByDoctorName(
            @PathVariable String firstname,
            @PathVariable String lastname) {
        List<DoctorDto> result = doctorService.findByDoctorName(firstname, lastname);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<DoctorDto>> findSpec() {
        List<DoctorDto> result = doctorService.findSpecializations();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{doctorId}/spec")
    public ResponseEntity<DoctorDto> getDoctorWithSpecializations(@PathVariable UUID doctorId) {
        DoctorDto doctorDto = doctorService.getDoctorWithSpecializations(doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    @GetMapping("/{doctorId}/available-schedules")
    public ResponseEntity<DoctorDto> getDoctorWithAvailableSchedules(@PathVariable UUID doctorId) {
        DoctorDto doctorDto = doctorService.getDoctorWithAvailableSchedules(doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    @GetMapping("/scheduleId/{scheduleUuid}")
    public ResponseEntity<DoctorDto> findSchedule(@PathVariable UUID scheduleUuid) {
        return ResponseEntity.ok(doctorService.findSchedule(scheduleUuid));
    }

    @DeleteMapping("/remove/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID doctorId) {
        doctorService.logicalRemove(doctorId);
        return ResponseEntity.ok("Doctor Removed With ID " + doctorId + " has been successfully deleted.");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
