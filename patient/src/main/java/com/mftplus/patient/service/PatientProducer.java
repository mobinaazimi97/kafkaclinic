package com.mftplus.patient.service;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mftplus.patient.dto.PatientRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;

//@Service
//@Slf4j
public class PatientProducer {
//    private static final String REQUEST_TOPIC = "patient-requests";
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final EncryptionService encryptionService;
//    private final ObjectMapper objectMapper;
//
//    public PatientProducer(KafkaTemplate<String, String> kafkaTemplate, EncryptionService encryptionService, ObjectMapper objectMapper) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.encryptionService = encryptionService;
//        this.objectMapper = objectMapper;
//        log.info("Patient Producer started{}",this);
//    }
//
//    public void sendPatientRequest(PatientRequest request) throws Exception {
//        String requestJson = objectMapper.writeValueAsString(request);
//        String encryptedRequest = encryptionService.encrypt(requestJson);
//        kafkaTemplate.send(REQUEST_TOPIC, request.getPatientId(), encryptedRequest);
//        log.info("Patient request sent to in Patient Producer {}",REQUEST_TOPIC);
//    }
}