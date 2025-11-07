package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.dtos.product.ProductSummaryDTO;
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
        Product product = movement.getProduct();

        ProductSummaryDTO productDTO = new ProductSummaryDTO(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getUnitOfMeasure()
        );

        return new MovementResponseDTO(
                movement.getId(),
                productDTO,
                movement.getMovementDate(),
                movement.getQuantity(),
                movement.getMovementType(),
                movement.getStatus()
        );
    }
}
