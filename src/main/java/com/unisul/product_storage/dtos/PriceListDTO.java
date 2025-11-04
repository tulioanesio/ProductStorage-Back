package com.unisul.product_storage.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceListDTO(

        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
        BigDecimal unitPrice,

        @NotBlank(message = "Unit cannot be blank")
        String unit,

        @NotBlank(message = "Category cannot be blank")
        String category
) {
}
