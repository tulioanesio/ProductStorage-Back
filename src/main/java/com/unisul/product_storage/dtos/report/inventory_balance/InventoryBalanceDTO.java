package com.unisul.product_storage.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo do balanço de estoque de um produto.")
public record InventoryBalanceDTO(

        @Schema(description = "Nome do produto.", example = "Monitor Samsung 24\"")
        String name,

        @Schema(description = "Quantidade disponível em estoque.", example = "35")
        int stockAvailable,

        @Schema(description = "Valor total do estoque do produto (quantidade x preço unitário).", example = "17499.65")
        BigDecimal totalValue
) {}
