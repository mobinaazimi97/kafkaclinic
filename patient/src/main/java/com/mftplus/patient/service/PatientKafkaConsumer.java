package com.mftplus.patient.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PatientKafkaConsumer {
    public PatientKafkaConsumer() {
        System.out.println("PatientKafkaConsumer Started!!");
    }

    private static final Logger logger = LoggerFactory.getLogger(PatientKafkaConsumer.class);

    @KafkaListener(topics = "patient-events", groupId = "patient-group")
    public void listen(String message) {
        logger.info("Received Patient Event {} ", message);

    }


}
//    @KafkaListener(topics = "appointment-events", groupId = "patient-group")
//    public void consumeAppointmentEvent(String message) {
//        System.out.println("Patient Service received: " + message);
//    }