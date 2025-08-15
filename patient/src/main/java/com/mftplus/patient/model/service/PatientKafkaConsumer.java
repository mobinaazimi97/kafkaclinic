package com.mftplus.patient.model.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatientKafkaConsumer {
    public PatientKafkaConsumer() {
        log.info("PatientKafkaConsumer Started!!");
    }

    @KafkaListener(topics = "patient-events", groupId = "patient-group")
    public void listen(String message) {
        log.info("Received Patient Event {} ", message);

    }
}
