package com.mftplus.patient.service;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mftplus.patient.dto.ChatMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;

//@Service
//@Slf4j
public class ChatService {


//    private static final String TOPIC = "chat-messages";
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public ChatService(KafkaTemplate<String, String> kafkaTemplate, SimpMessagingTemplate messagingTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.messagingTemplate = messagingTemplate;
//        log.info("START CHAT SERVICE {}", this);
//
//    }
//
//    public void sendMessage(ChatMessage message) throws Exception {
//        log.info("SENDING MESSAGE IN CHAT SERVICE{}", message);
//        String messageJson = objectMapper.writeValueAsString(message);
//        kafkaTemplate.send(TOPIC, message.getSenderId(), messageJson);
//    }
//
//    @KafkaListener(topics = TOPIC, groupId = "chat-group")
//    public void consumeMessage(String messageJson) throws Exception {
//        log.info("Kafka Listener Start{}", messageJson);
//        ChatMessage message = objectMapper.readValue(messageJson, ChatMessage.class);
//        if (message.getRecipientId() == null) {
//            // پیام برای همه
//            messagingTemplate.convertAndSend("/topic/public", message);
//        } else {
//            log.info("Kafka Listener Received Message For Custom User{}", message);
//            // پیام برای کاربر خاص
//            messagingTemplate.convertAndSendToUser(
//                    message.getRecipientId(),
//                    "/topic/private",
//                    message
//            );
//        }
//    }
}