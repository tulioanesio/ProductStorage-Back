package com.unisul.product_storage.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resumo do produto vinculado ao movimento.")
public record ProductSummaryDTO(

        @Schema(description = "Identificador único do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Notebook Dell Inspiron 15")
        String name,

        @Schema(description = "Preço unitário do produto", example = "4299.99")
        BigDecimal unitPrice,

        @Schema(description = "Unidade de medida do produto", example = "unidade")
        String unitOfMeasure
) {}
