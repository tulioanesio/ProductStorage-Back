package com.unisul.product_storage.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produtos com nível de estoque abaixo do mínimo definido.")
public record LowStockProductsDTO(

        @Schema(description = "Nome do produto.", example = "Mouse Logitech M170")
        String name,

        @Schema(description = "Quantidade atual do produto.", example = "3")
        int quantity,

        @Schema(description = "Quantidade mínima recomendada em estoque.", example = "5")
        int minStockQuantity
) {}