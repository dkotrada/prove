package com.prove.prove.payment;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String paymentId) {
        super("Payment with ID " + paymentId + " not found");
    }
}
