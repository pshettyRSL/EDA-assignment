package com.example.ordernotificationsystem.event;

import java.math.BigDecimal;
import java.util.List;

public record OrderPayload(
        String orderId,
        String customerId,
        String customerEmail,
        BigDecimal totalAmount,
        List<String> items
) {
}
