package com.mftplus.appointment.model.service;

import com.mftplus.appointment.dto.AppointmentDto;
import com.mftplus.appointment.mapper.AppointmentMapper;
import com.mftplus.appointment.model.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppointmentKafkaConsumer {
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;

    public AppointmentKafkaConsumer(AppointmentMapper appointmentMapper, AppointmentRepository appointmentRepository) {
        this.appointmentMapper = appointmentMapper;
        this.appointmentRepository = appointmentRepository;
    }

    @KafkaListener(topics = "appointment-events", groupId = "appointment-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(AppointmentDto appointmentDto) {
        if (appointmentDto == null) {
            log.info("received message is null ");
            return;
        }

        try {
            log.info("Appointment Message  " + appointmentDto);
            appointmentRepository.save(appointmentMapper.toEntity(appointmentDto));

        } catch (Exception e) {
            log.info("Save Appointment Error: {}" , e.getMessage());
        }
    }
}