package com.prove.prove.payment.adapter;

public record PaymentCreatedResponse(
        String paymentId,
        String status
) { }
