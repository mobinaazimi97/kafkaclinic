package com.mftplus.patient.model.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatientKafkaProducer {

    public PatientKafkaProducer() {
        log.info("PatientKafkaProducer Start!!!!");

    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String appointmentJson) {
        kafkaTemplate.send("appointment-events", appointmentJson);
        log.info("Sent to appointment-events : {} ", appointmentJson);
    }
}
