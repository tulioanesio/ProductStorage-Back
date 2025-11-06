package com.unisul.product_storage.dtos.report;

public record LowStockProductsDTO(
        String name,
        int quantity,
        int minStockQuantity
) {
}
