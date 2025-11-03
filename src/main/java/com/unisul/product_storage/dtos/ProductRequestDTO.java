package com.unisul.product_storage.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(

        @NotBlank(message = "Product name is required.")
        @Size(max = 100, message = "Product name must not exceed 100 characters.")
        String name,

        @NotNull(message = "Unit price is required.")
        @DecimalMin(value = "0.01", message = "Unit price must be greater than zero.")
        BigDecimal unitPrice,

        @NotBlank(message = "Unit of measure is required.")
        @Size(max = 10, message = "Unit must not exceed 10 characters.")
        String unit,

        @Min(value = 0, message = "Stock quantity cannot be negative.")
        int stockQuantity,

        @Min(value = 0, message = "Minimum stock quantity cannot be negative.")
        int minStockQuantity,

        @Min(value = 1, message = "Maximum stock quantity must be at least 1.")
        int maxStockQuantity,

        @NotBlank(message = "Category is required.")
        @Size(max = 50, message = "Category must not exceed 50 characters.")
        String category
) {}
