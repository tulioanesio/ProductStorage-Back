package com.unisul.product_storage.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;

@Schema(description = "Resposta completa do relatório de balanço de estoque.")
public record InventoryBalanceResponseDTO(

        @Schema(description = "Valor total de todos os produtos em estoque.", example = "152345.20")
        BigDecimal stockValue,

        @Schema(description = "Lista paginada com o balanço de cada produto.")
        Page<InventoryBalanceDTO> items
) {}