package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.ScheduleDto;
import com.mftplus.appointment.model.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
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

    @GetMapping("/specializations/{specializationId}")
    public ResponseEntity<?> findAvailableSchedulesBySpecialization(@PathVariable("specializationId") UUID specializationId) {
        try {
            List<ScheduleDto> scheduleDtos = scheduleService.findAvailableSchedulesBySpecialization(specializationId);

            if (scheduleDtos == null || scheduleDtos.isEmpty()) {
                String reason = (scheduleDtos == null) ? "No data found (null returned)"
                        : "No available schedules for this specialization";
                Map<String, String> response = new HashMap<>();
                response.put("message", reason);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // اگر لیست پر بود، درست برگردون
            return ResponseEntity.ok(scheduleDtos);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/remove/{scheduleId}")
    public ResponseEntity<Void> delete(@PathVariable UUID scheduleId) {
        scheduleService.logicalRemove(scheduleId);
        return ResponseEntity.noContent().build();
    }

}
