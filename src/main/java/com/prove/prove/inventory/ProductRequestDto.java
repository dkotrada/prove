package com.prove.prove.inventory;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestDto(
        @NotBlank String productId,
        @NotBlank String name,
        int quantity,
        double price
) {
}
