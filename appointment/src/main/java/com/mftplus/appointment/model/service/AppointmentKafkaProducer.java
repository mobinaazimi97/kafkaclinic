package com.mftplus.appointment.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mftplus.appointment.dto.AppointmentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppointmentKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    public AppointmentKafkaProducer(ObjectMapper mapper) {
        this.mapper = mapper;
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        log.info("AppointmentKafkaProducer Start!!!!");
    }


    public void sendData(AppointmentDto requestDto) {
        try {
            String json = mapper.writeValueAsString(requestDto);
            kafkaTemplate.send("appointment-events", json);
            log.info("Appointment sent to Kafka: {}", json);

        } catch (JsonProcessingException e) {
            log.error("Error serializing AppointmentDto: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error sending AppointmentDto to Kafka: {}", e.getMessage(), e);
        }
    }
}
