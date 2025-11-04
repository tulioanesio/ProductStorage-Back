package com.unisul.product_storage.dtos.movement;

import com.unisul.product_storage.models.MovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record MovementRequestDTO(
        @NotNull(message = "Product ID must not be null")
        Long productId,

        @NotNull(message = "Movement date must not be null")
        @PastOrPresent(message = "Movement date cannot be in the future")
        LocalDate movementDate,

        @NotNull(message = "Quantity must not be null")
        @Min(value = 0, message = "Quantity must be greater than zero")
        int quantity,

        @NotNull(message = "Movement type must not be null")
        MovementType movementType
) {}
