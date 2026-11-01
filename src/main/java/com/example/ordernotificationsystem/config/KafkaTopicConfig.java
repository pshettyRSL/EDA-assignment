package com.example.ordernotificationsystem.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public static final String ORDER_PLACED_TOPIC = "order-placed-topic";
    public static final String USER_REGISTERED_TOPIC = "user.registered.topic";

    @Bean
    public NewTopic orderPlacedTopic() {
        return TopicBuilder.name(ORDER_PLACED_TOPIC)
                .partitions(3)          // Enables parallel consumer processing
                .replicas(1)            // Single replica for local dev
                .build();
    }

    @Bean
    public NewTopic userRegistered() {
        return TopicBuilder.name(USER_REGISTERED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
