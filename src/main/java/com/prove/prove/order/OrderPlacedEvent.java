package com.prove.prove.order;

import com.prove.prove.order.internal.OrderItem;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public record OrderPlacedEvent(
        String orderId,
        List<OrderItem> items,
        String customerId
) { }

