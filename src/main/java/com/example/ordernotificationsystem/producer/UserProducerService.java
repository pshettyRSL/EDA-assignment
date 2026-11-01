package com.example.ordernotificationsystem.producer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProducerService {
    private static final Logger log = LoggerFactory.getLogger(UserProducerService.class);
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public void publishUserRegistrationEvent(UserRegisteredEvent event) {
        kafkaTemplate.send(KafkaTopicConfig.USER_REGISTERED_TOPIC, event.email(), event);
        log.info("[PRODUCER] Event published successfully to topic '{}'", KafkaTopicConfig.USER_REGISTERED_TOPIC);
    }
}
