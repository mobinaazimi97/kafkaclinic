package com.mftplus.appointment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentKafkaConsumer {
    public AppointmentKafkaConsumer() {
        System.out.println("Appointment Consumer Started!!!");
    }

    private static final Logger logger = LoggerFactory.getLogger(AppointmentKafkaConsumer.class);

    @KafkaListener(topics = "appointment-events", groupId = "appointment-group")
    public void listen(String message) {
        logger.info("Received Appointment Message {} ", message);
    }

}
