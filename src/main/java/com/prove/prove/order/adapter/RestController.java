package com.prove.prove.order.adapter;

import com.prove.prove.order.OrderRequestDto;
import com.prove.prove.order.OrderResponseDto;
import com.prove.prove.order.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/orders")
public class RestController {
    private final OrderService orderService;

    public RestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequestDto request) {
        String orderId = UUID.randomUUID().toString();
        orderService.placeOrder(orderId, request.items(), request.customerId());
        return ResponseEntity.created(URI.create("/orders/" + orderId)).body(orderId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable @NotBlank(message = "Order ID cannot be blank") String id) {
        OrderResponseDto dto = orderService.findOrderByIdOrThrow(id);
        return ResponseEntity.ok(dto);
    }

}