package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.Product;

public class MovementMapper {

    private MovementMapper() {}

    public static Movement toEntity(MovementRequestDTO data, Product product) {
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setMovementDate(data.movementDate());
        movement.setQuantity(data.quantity());
        movement.setMovementType(data.movementType());
        return movement;
    }

    public static MovementResponseDTO toResponseDTO(Movement movement) {
        return new MovementResponseDTO(
                movement.getId(),
                movement.getProduct(),
                movement.getMovementDate(),
                movement.getQuantity(),
                movement.getMovementType(),
                movement.getStatus()
        );
    }
}
