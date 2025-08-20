package com.prove.prove.payment;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        String paymentId,
        String orderId,
        double amount,
        String status,
        LocalDateTime paymentDate
) {
}
