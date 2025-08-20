package com.prove.prove.payment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(
        @NotBlank(message = "Order ID can not be blank") String orderId,
        @Positive(message = "Amount must be positive") double amount
) { }
