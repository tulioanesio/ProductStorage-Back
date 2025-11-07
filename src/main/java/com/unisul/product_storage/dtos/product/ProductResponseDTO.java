package com.unisul.product_storage.dtos.product;

import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resposta detalhada de um produto, incluindo informações de estoque e categoria.")
public record ProductResponseDTO(

        @Schema(description = "Identificador único do produto.", example = "101")
        Long id,

        @Schema(description = "Nome do produto.", example = "Notebook Dell Inspiron 15")
        String name,

        @Schema(description = "Preço unitário do produto.", example = "4299.99")
        BigDecimal unitPrice,

        @Schema(description = "Unidade de medida do produto.", example = "unidade")
        String unitOfMeasure,

        @Schema(description = "Quantidade disponível em estoque.", example = "20")
        int availableStock,

        @Schema(description = "Quantidade mínima recomendada em estoque.", example = "5")
        int minQuantity,

        @Schema(description = "Quantidade máxima recomendada em estoque.", example = "50")
        int maxQuantity,

        @Schema(description = "Categoria vinculada ao produto.")
        CategoryResponseDTO category
) {}