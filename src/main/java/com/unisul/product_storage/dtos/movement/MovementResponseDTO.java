package com.unisul.product_storage.dtos.movement;

import com.unisul.product_storage.dtos.product.ProductSummaryDTO;
import com.unisul.product_storage.models.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Resposta detalhada de um movimento de produto.")
public record MovementResponseDTO(

        @Schema(description = "Identificador único do movimento", example = "10")
        Long id,

        @Schema(description = "Produto associado ao movimento")
        ProductSummaryDTO product,

        @Schema(description = "Data do movimento (entrada/saída)", example = "2025-11-04")
        LocalDate movementDate,

        @Schema(description = "Quantidade movimentada", example = "15")
        Integer quantity,

        @Schema(description = "Tipo do movimento: ENTRY ou EXIT", example = "ENTRY")
        MovementType movementType,

        @Schema(description = "Status atual do movimento", example = "Normal")
        String status
) {}
