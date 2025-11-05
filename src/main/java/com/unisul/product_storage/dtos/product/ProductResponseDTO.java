package com.unisul.product_storage.dtos.product;

import com.unisul.product_storage.dtos.categoria.CategoryResponseDTO;
import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        BigDecimal unitPrice,
        String unitOfMeasure,
        int stockAvailable,
        int minQuantity,
        int maxQuantity,
        CategoryResponseDTO category
) {}
