package com.prove.prove.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        String orderId,
        String customerId,
        double totalAmount,
        LocalDateTime orderDate,
        List<OrderItemDto> items
) {
}
