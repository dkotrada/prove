package com.prove.prove.order;

import java.util.List;

public record OrderPlacedEvent(
        String orderId,
        List<OrderItemDto> items,
        String customerId
) { }

