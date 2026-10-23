package com.example.ordernotificationsystem.event;

import java.time.Instant;
import java.util.UUID;

public record OrderPlacedEvent(
        String eventId,
        String eventType,
        Instant timestamp,
        OrderPayload payload
) {
    /**
     * Factory method to create a new event with auto-generated ID and timestamp.
     */
    public static OrderPlacedEvent create(OrderPayload payload) {
        return new OrderPlacedEvent(
                UUID.randomUUID().toString(),
                "OrderPlaced",
                Instant.now(),
                payload
        );
    }
}