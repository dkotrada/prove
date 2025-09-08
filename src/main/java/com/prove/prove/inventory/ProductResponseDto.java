package com.prove.prove.inventory;

import jakarta.validation.constraints.NotBlank;

public record ProductResponseDto(
        @NotBlank String productId,
        @NotBlank String name,
        int quantity,
        double price
) {
}
