package com.example.ordernotificationsystem.event;

import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent (
        String eventId,
        String eventType,
        Instant timestamp,
        String email) {
    /**
     * Factory method to create a new event with auto-generated ID and timestamp.
     */
    public static UserRegisteredEvent create(@NonNull String email) {
        return new UserRegisteredEvent(
                UUID.randomUUID().toString(),
                "UserRegistered",
                Instant.now(),
                email
        );
    }
}
