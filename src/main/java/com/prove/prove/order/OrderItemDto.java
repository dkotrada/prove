package com.prove.prove.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OrderItemDto(
        @NotBlank(message = "Product ID cannot be blank")
        String productId,

        @Positive(message = "Quantity must be positive")
        int quantity,

        @Positive(message = "Price must be positive")
        double price
) {}