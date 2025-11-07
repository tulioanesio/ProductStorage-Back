package com.unisul.product_storage.dtos.report.products_by_category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de produtos agrupados por categoria.")
public record ProductsByCategoryResponseDTO(

        @Schema(description = "Nome da categoria.", example = "Periféricos")
        String name,

        @Schema(description = "Quantidade total de produtos pertencentes à categoria.", example = "12")
        int quantity
) {}