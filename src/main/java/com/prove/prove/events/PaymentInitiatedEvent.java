package com.prove.prove.events;

public record PaymentInitiatedEvent(
        String paymentId,
        String orderId,
        double amount
) {
}
