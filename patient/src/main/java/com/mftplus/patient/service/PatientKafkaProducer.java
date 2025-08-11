package com.mftplus.patient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.patient.dto.PatientDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientKafkaProducer {

    public PatientKafkaProducer() {
        System.out.println("PatientKafkaProducer Start!!!!");

    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String appointmentJson) {
        kafkaTemplate.send("appointment-events", appointmentJson);
        System.out.println("Sent to appointment-events: " + appointmentJson);
    }


//    public void sendPatientData(String patientJson) {
//        kafkaTemplate.send("patient-events", patientJson);
//    }

//    private static final Logger logger = LoggerFactory.getLogger(PatientKafkaProducer.class);
//    private static final String TOPIC = "patient-events";
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public void sendPatientEvent(PatientDto patientDto) {
//        logger.info("ارسال رویداد بیمار با شناسه {} به Kafka", patientDto.getPatientId());
//        kafkaTemplate.send(TOPIC, patientDto.getPatientId().toString(), patientDto);
//    }

//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public PatientKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

//    public void sendPatientCreatedEvent(String patientJson) {
//        kafkaTemplate.send("patient-events", patientJson);
//    }
}
