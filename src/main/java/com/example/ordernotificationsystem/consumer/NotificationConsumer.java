package com.example.ordernotificationsystem.consumer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class NotificationConsumer {

    private static Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "notification-consumer-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("──────────────────────────────────────────────────");
        log.info("[NOTIFICATION SERVICE] Event received");
        log.info("[NOTIFICATION SERVICE]   Event ID   : {}", event.eventId());
        log.info("[NOTIFICATION SERVICE]   Event Type : {}", event.eventType());
        log.info("[NOTIFICATION SERVICE]   Timestamp  : {}", event.timestamp());
        log.info("[NOTIFICATION SERVICE]   Order ID   : {}", event.payload().orderId());
        log.info("[NOTIFICATION SERVICE]   Customer   : {}", event.payload().customerEmail());
        log.info("[NOTIFICATION SERVICE]   Total      : ${}", event.payload().totalAmount());
        log.info("[NOTIFICATION SERVICE]   Items      : {}", event.payload().items());
        log.info("[NOTIFICATION SERVICE] Confirmation email sent to {}",
                event.payload().customerEmail());
        log.info("──────────────────────────────────────────────────");
    }
}
