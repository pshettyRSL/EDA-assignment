package com.example.ordernotificationsystem.consumer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.event.UserRegisteredEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @Transactional
    @KafkaListener(topics = KafkaTopicConfig.USER_REGISTERED_TOPIC, groupId = "email-consumer-group")
    public void handleUserRegistration(UserRegisteredEvent event) {
        log.info("──────────────────────────────────────────────────");
        log.info("[EMAIL SERVICE] Event received");
        log.info("[EMAIL SERVICE]   Event ID   : {}", event.eventId());
        log.info("[EMAIL SERVICE]   Event Type : {}", event.eventType());
        log.info("[EMAIL SERVICE]   Timestamp  : {}", event.timestamp());
        log.info("[EMAIL SERVICE]   Mail sent to mail ID   : {}", event.email());
        log.info("──────────────────────────────────────────────────");
    }
}
