package com.unisul.product_storage.dtos;

import jdk.jfr.Category;

import java.math.BigDecimal;

public record ProductRequestDTO(
        String name,
        BigDecimal unitPrice,
        String unit,
        int stockQuantity,
        int minStockQuantity,
        int maxStockQuantity,
        String category
) {
}
