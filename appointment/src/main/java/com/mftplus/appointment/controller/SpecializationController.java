package com.mftplus.appointment.controller;

import com.mftplus.appointment.dto.SpecializationDto;
import com.mftplus.appointment.model.service.SpecializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specializations")
@Slf4j
public class SpecializationController {
    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping("/save")
    public ResponseEntity<SpecializationDto> post(@RequestBody SpecializationDto specializationDto) {
        SpecializationDto savedSpecialization = specializationService.save(specializationDto);
        log.info("savedSpecialization {}", savedSpecialization);
        return ResponseEntity.ok().body(savedSpecialization);
    }

    @PutMapping("/edit/{specializationId}")
    public ResponseEntity<SpecializationDto> update(@PathVariable UUID specializationId, @RequestBody SpecializationDto specializationDto) {
        SpecializationDto updated = specializationService.update(specializationId, specializationDto);
        return ResponseEntity.ok().body(updated);
    }

    @GetMapping
    public ResponseEntity<List<SpecializationDto>> getAll() {
        List<SpecializationDto> specializationDtos = specializationService.findAll();
        log.info("Found All Specializations" + specializationDtos);
        return ResponseEntity.ok().body(specializationDtos);
    }

    @GetMapping("/{specializationId}")
    public ResponseEntity<SpecializationDto> getSpecializationById(@PathVariable UUID specializationId) {
        SpecializationDto specializationDto = specializationService.findById(specializationId);
        return ResponseEntity.ok().body(specializationDto);
    }

    @GetMapping("/skill/{skillName}")
    public ResponseEntity<List<SpecializationDto>> findBySkillName(@PathVariable String skillName) {
        List<SpecializationDto> specializationDtoList = specializationService.findBySkillName(skillName);
        return ResponseEntity.ok().body(specializationDtoList);
    }

    @DeleteMapping("/remove/{specializationId}")
    public ResponseEntity<Void> deleteSpecializationById(@PathVariable UUID specializationId) {
        specializationService.logicalRemove(specializationId);
        return ResponseEntity.noContent().build();
    }
}

