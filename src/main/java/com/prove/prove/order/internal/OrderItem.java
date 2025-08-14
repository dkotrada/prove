package com.prove.prove.order.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record OrderItem(
        @Column(name = "product_id", nullable = false)
        String productId,
        @Column(nullable = false)
        int quantity,
        @Column(nullable = false)
        double price
) {
}
