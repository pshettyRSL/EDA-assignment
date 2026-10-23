package com.example.ordernotificationsystem.producer;

import com.example.ordernotificationsystem.config.KafkaTopicConfig;
import com.example.ordernotificationsystem.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class OrderProducerService {
    private static final Logger log = LoggerFactory.getLogger(OrderProducerService.class);
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderProducerService(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrder(@RequestBody OrderPlacedEvent event) {
        kafkaTemplate.send(KafkaTopicConfig.ORDER_PLACED_TOPIC, event.payload().orderId(), event);
        log.info("[PRODUCER] Event published successfully to topic '{}'", KafkaTopicConfig.ORDER_PLACED_TOPIC);
    }
}
