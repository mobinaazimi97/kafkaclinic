package com.mftplus.patient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mftplus.patient.dto.PatientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

//@Service
//@Slf4j
public class PatientConsumer {
//    private final SimpMessagingTemplate messagingTemplate;
//    private final EncryptionService encryptionService;
//    private final ObjectMapper objectMapper;
//
//    public PatientConsumer(SimpMessagingTemplate messagingTemplate, EncryptionService encryptionService, ObjectMapper objectMapper) {
//        this.messagingTemplate = messagingTemplate;
//        this.encryptionService = encryptionService;
//        this.objectMapper = objectMapper;
//        log.info("Patient Consumer started in CheckConsumer");
//    }
//
//    @KafkaListener(topics = "patient-responses", groupId = "patient-group")
//    public void consume(String encryptedResponse) throws Exception {
//        log.info("(kafka Listener in PatientConsumer)PatientConsumer received encrypted response: {}", encryptedResponse);
//        String decryptedResponse = encryptionService.decrypt(encryptedResponse);
//        PatientResponse response = objectMapper.readValue(decryptedResponse, PatientResponse.class);
//        messagingTemplate.convertAndSend("/topic/patients-responses", response);
//    }
}