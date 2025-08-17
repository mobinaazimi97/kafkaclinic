package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.model.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/doctor/{doctorId}/start/{start}/end/{end}/duration/{appointmentDurationMin}")
    public ResponseEntity<List<ScheduleDto>> post(@PathVariable UUID doctorId, @PathVariable LocalDateTime start, @PathVariable LocalDateTime end, @PathVariable int appointmentDurationMin) {
        List<ScheduleDto> savedScheduleDto = scheduleService.createSchedulesForDoctor(doctorId, start, end, appointmentDurationMin);
        return ResponseEntity.ok(savedScheduleDto);
    }

    @PutMapping("/edit/{scheduleId}")
    public ResponseEntity<ScheduleDto> put(@RequestBody ScheduleDto scheduleDto, @PathVariable UUID scheduleId) {
        ScheduleDto updated = scheduleService.update(scheduleId, scheduleDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getAll() {
        List<ScheduleDto> scheduleDtos = scheduleService.findAll();
        return ResponseEntity.ok(scheduleDtos);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDto> getScheduleById(@PathVariable UUID scheduleId) {
        ScheduleDto schedule = scheduleService.getById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/spec/{specializationId}")
    public ResponseEntity<List<ScheduleDto>> findAvailableSchedulesBySpecialization(@PathVariable UUID specializationId) {
        List<ScheduleDto> scheduleDtos = scheduleService.findAvailableSchedulesBySpecialization(specializationId);
        return ResponseEntity.ok(scheduleDtos);

    }


    @DeleteMapping("/remove/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable UUID scheduleId) {
        scheduleService.logicalRemove(scheduleId);
        return ResponseEntity.noContent().build();
    }

}
