package com.example.ordernotificationsystem.consumer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.entity.ProcessedEvent;
import com.example.ordernotificationsystem.event.OrderPlacedEvent;
import com.example.ordernotificationsystem.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AnalyticsConsumer {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsConsumer.class);
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    @KafkaListener(
            topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
            groupId = "analytics-consumer-group"
    )
    public void recordAnalytics(OrderPlacedEvent event) {
        String eventId = event.eventId();

        log.info("══════════════════════════════════════════════════");
        log.info("[ANALYTICS SERVICE] Event received: ID={}", eventId);

        // Idempotency check
        if (processedEventRepository.existsById(eventId)) {
            log.warn("[ANALYTICS SERVICE]    IDEMPOTENCY TRIGGERED!");
            log.warn("[ANALYTICS SERVICE]    Event ID '{}' has ALREADY been processed.", eventId);
            log.warn("[ANALYTICS SERVICE]    Skipping to prevent duplicate side effects.");
            log.info("══════════════════════════════════════════════════");
            return;
        }

        // Process Event
        log.info("[ANALYTICS SERVICE] Processing new event...");
        log.info("[ANALYTICS SERVICE]   Order ID    : {}", event.payload().orderId());
        log.info("[ANALYTICS SERVICE]   Customer ID : {}", event.payload().customerId());
        log.info("[ANALYTICS SERVICE]   Items Count : {}", event.payload().items().size());
        log.info("[ANALYTICS SERVICE]   Revenue     : ${}", event.payload().totalAmount());
        log.info("[ANALYTICS SERVICE]   Timestamp   : {}", event.timestamp());

        processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));

        log.info("[ANALYTICS SERVICE] Event ID '{}' processed and recorded in idempotency store.", eventId);
        log.info("══════════════════════════════════════════════════");
    }
}
