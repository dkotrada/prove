package com.prove.prove.order.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Embeddable
public record OrderItem(

        @NotBlank(message = "Product ID cannot be blank")
        @Column(name = "product_id", nullable = false)
        String productId,

        @PositiveOrZero(message = "Quantity cannot be negative")
        @Column(nullable = false)
        int quantity,
        @Column(nullable = false)
        double price
) {
}
