package com.prove.prove.payment.adapter;

import com.prove.prove.payment.PaymentRequestDto;
import com.prove.prove.payment.PaymentResponseDto;
import com.prove.prove.payment.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payments")
public class PaymentRestApi {
    private final PaymentService paymentService;

    public PaymentRestApi(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@Valid @RequestBody PaymentRequestDto request) {
        String paymentId = paymentService.processPayment(request);
        return ResponseEntity.created(URI.create("/payments/" + paymentId)).body(paymentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPayment(
            @PathVariable @NotNull(message = "Payment ID can not be blank") String paymentId
    ) {
        PaymentResponseDto dto = paymentService.findPaymentByIdOrThrow(paymentId);
        return ResponseEntity.ok(dto);
    }
}
