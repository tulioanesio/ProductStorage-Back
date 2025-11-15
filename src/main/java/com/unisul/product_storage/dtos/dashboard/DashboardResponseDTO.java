package com.unisul.product_storage.dtos.dashboard;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record DashboardResponseDTO(

        @Min(value = 0, message = "Total de produtos não pode ser negativo.")
        long totalProducts,

        @Min(value = 0, message = "Quantidade de produtos com baixo estoque não pode ser negativa.")
        long lowStockProducts,

        @Min(value = 0, message = "Quantidade de produtos com alto estoque não pode ser negativa.")
        long highStockProducts,

        @NotNull(message = "O valor total do estoque não pode ser nulo.")
        @PositiveOrZero(message = "O valor total do estoque não pode ser negativo.")
        BigDecimal totalStockValue

) {}