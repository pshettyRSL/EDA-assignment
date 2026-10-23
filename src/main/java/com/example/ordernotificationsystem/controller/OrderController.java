package com.example.ordernotificationsystem.controller;

import com.example.ordernotificationsystem.event.OrderPayload;
import com.example.ordernotificationsystem.event.OrderPlacedEvent;
import com.example.ordernotificationsystem.producer.OrderProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/orders")
@RequiredArgsConstructor
public class OrderController {
    public final OrderProducerService orderProducerService;

    @PostMapping(path = "/")
    public ResponseEntity<OrderPlacedEvent> placeOrder(@RequestBody OrderPayload payload) {
        OrderPlacedEvent event = OrderPlacedEvent.create(payload);
        orderProducerService.publishOrder(event);
        return ResponseEntity.ok(event);
    }

    @PostMapping(path = "/publish-duplicate-event")
    public ResponseEntity<?> publishDuplicateEvent(@RequestBody OrderPlacedEvent event) {
        orderProducerService.publishOrder(event);
        return ResponseEntity.ok(Map.of(
                "message", "Duplicate event re-published for idempotency testing",
                "eventId", event.eventId()
        ));
    }
}
