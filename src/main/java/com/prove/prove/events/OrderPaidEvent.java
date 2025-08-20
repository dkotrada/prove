package com.prove.prove.events;

public record OrderPaidEvent(
        String orderId,
        double amount
) {
}
