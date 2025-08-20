package com.prove.prove.events;

public record PaymentValidatedEvent(
        String paymentId,
        String orderId,
        double amount
) {
}
