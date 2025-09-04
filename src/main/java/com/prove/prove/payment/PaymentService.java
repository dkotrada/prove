package com.prove.prove.payment;

import com.prove.prove.events.OrderPaidEvent;
import com.prove.prove.events.PaymentInitiatedEvent;
import com.prove.prove.events.PaymentValidatedEvent;
import com.prove.prove.payment.internal.Payment;
import com.prove.prove.payment.internal.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
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
        logger.debug("Processing payment for order {} with amount {}", request.orderId(), request.amount());

        if (request.amount() <= 0) {
            logger.warn("Invalid payment amount {}", request.amount());
            throw new PaymentFailedException("Payment amount must be positive");
        }

        String paymentId = UUID.randomUUID().toString();
        Payment payment = Payment.create(paymentId, request.orderId(), request.amount());
        payment.setStatus("PENDING");
        paymentRepository.save(payment);
        logger.info("Payment {} saved with status {}", payment.getPaymentId(), payment.getStatus());

        eventPublisher.publishEvent(new PaymentInitiatedEvent(payment.getPaymentId(), request.orderId(), request.amount()));

        return payment.getPaymentId();
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto findPaymentByIdOrThrow(String paymentId) {
        logger.debug("Fetching payment {}", paymentId);
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

    @EventListener
    @Transactional
    public void handlePaymentInitiatedEvent(PaymentInitiatedEvent event) {
        logger.debug("PaymentInitiatedEvent for {}", event.paymentId());
        // Mock gateway simulation
        boolean paymentSuccessful = mockPaymentGateway(event.orderId(), event.amount());
        if(paymentSuccessful) {
            eventPublisher.publishEvent(new PaymentValidatedEvent(event.paymentId(), event.orderId(), event.amount()));
        } else {
            int updated = paymentRepository.updateStatus(event.paymentId(), "FAILED");
            if (updated == 0) {
                throw new PaymentNotFoundException(event.paymentId());
            }
            logger.warn("Payment {} failed", event.paymentId());
        }
    }

    @EventListener
    @Transactional
    public void handlePaymentValidatedEvent(PaymentValidatedEvent event) {
        logger.debug("PaymentValidatedEvent for {}", event.paymentId());

        Payment payment = paymentRepository.findById(event.paymentId())
                .orElseThrow(() -> new PaymentNotFoundException(event.paymentId()));
        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new OrderPaidEvent(event.orderId(), event.amount()));
    }

    private boolean mockPaymentGateway(String orderId, double amount) {
        // Mock behavior of a gateway: accept all payments below 1000 Euro
        return amount < 1000;
    }
}
