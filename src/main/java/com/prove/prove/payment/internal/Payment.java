package com.prove.prove.payment.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @NotBlank(message = "Payment ID can not be blank")
    String paymentId;

    @NotBlank(message = "Order ID can not be blank")
    @Column(name = "order_id", nullable = false)
    String orderId;

    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    double amount;

    @NotBlank(message = "Status can not be blank")
    @Column(nullable = false)
    String status;

    @Column(name = "payment_date", nullable = false)
    LocalDateTime paymentDate;

    public Payment() {
    }

    private Payment(
            String paymentId,
            String orderId,
            double amount,
            String status,
            LocalDateTime paymentDate
    ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    public static Payment create(String paymentId, String orderId, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        return new Payment(paymentId, orderId, amount, "COMPLETED", LocalDateTime.now());
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
