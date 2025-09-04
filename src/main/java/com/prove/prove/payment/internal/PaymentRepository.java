package com.prove.prove.payment.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Modifying
    @Query("update Payment p set p.status = :status where p.paymentId = :paymentId")
    int updateStatus(String paymentId, String status);
}
