package com.unisul.product_storage.dtos.movement;

import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;

import java.time.LocalDate;

public record MovementResponseDTO(
        Long id,
        Product product,
        LocalDate movementDate,
        Integer quantity,
        MovementType movementType,
        String status
) {
}
