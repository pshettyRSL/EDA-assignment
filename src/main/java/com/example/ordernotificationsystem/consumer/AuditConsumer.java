package com.example.ordernotificationsystem.consumer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditConsumer {
    private static final Logger log = LoggerFactory.getLogger(AuditConsumer.class);

    @KafkaListener(topics = KafkaTopicConfig.USER_REGISTERED_TOPIC, groupId = "audit-consumer-group")
    public void logRegisteredUser(UserRegisteredEvent event) {
        log.info("──────────────────────────────────────────────────");
        log.info("[AUDIT SERVICE] Event received");
        log.info("[AUDIT SERVICE]   Event ID   : {}", event.eventId());
        log.info("[AUDIT SERVICE]   Event Type : {}", event.eventType());
        log.info("[AUDIT SERVICE]   Timestamp  : {}", event.timestamp());
        log.info("[AUDIT SERVICE]   Mail ID   : {}", event.email());
        log.info("──────────────────────────────────────────────────");
    }
}
