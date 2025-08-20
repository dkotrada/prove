package com.prove.prove.events;

import com.tagitech.provelib.dto.OrderItemDto;

import java.util.List;

public record OrderPlacedEvent(
        String orderId,
        List<OrderItemDto> items,
        String customerId
) { }

