package com.prove.prove.payment;

import com.prove.prove.events.OrderPaidEvent;
import com.prove.prove.events.PaymentValidatedEvent;
import com.prove.prove.payment.internal.Payment;
import com.prove.prove.payment.internal.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentRepository paymentRepository;

    public PaymentService(
            ApplicationEventPublisher eventPublisher,
            PaymentRepository paymentRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public String processPayment(PaymentRequestDto request) {
        if (request.amount() <= 0) {
            throw new PaymentFailedException("Payment amount must be positive");
        }
        String paymentId = UUID.randomUUID().toString();
        Payment payment = Payment.create(paymentId, request.orderId(), request.amount());
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new OrderPaidEvent(request.orderId(), request.amount()));
        return paymentId;
    }

    @EventListener
    @Transactional
    public void handlePaymentValidatedEvent(PaymentValidatedEvent event) {
        Payment payment = Payment.create(event.paymentId(), event.orderId(), event.amount());
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new OrderPaidEvent(event.orderId(), event.amount()));
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto findPaymentByIdOrThrow(String paymentId) {
        return paymentRepository.findById(paymentId)
                .map(payment -> new PaymentResponseDto(
                        payment.getPaymentId(),
                        payment.getOrderId(),
                        payment.getAmount(),
                        payment.getStatus(),
                        payment.getPaymentDate()
                ))
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }
}
