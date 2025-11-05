package com.unisul.product_storage.dtos.movement;

import com.unisul.product_storage.models.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Schema(description = "Requisição para registrar um movimento de entrada ou saída de produto no estoque.")
public record MovementRequestDTO(

        @Schema(
                description = "Identificador do produto relacionado ao movimento de estoque.",
                example = "1",
                required = true
        )
        @NotNull(message = "O ID do produto não pode ser nulo.")
        Long productId,

        @Schema(
                description = "Data do movimento (não pode ser futura).",
                example = "2025-11-04",
                required = true
        )
        @NotNull(message = "A data do movimento é obrigatória.")
        @PastOrPresent(message = "A data do movimento não pode estar no futuro.")
        LocalDate movementDate,

        @Schema(
                description = "Quantidade de produtos movimentados (entrada ou saída).",
                example = "10",
                minimum = "0",
                required = true
        )
        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 0, message = "A quantidade deve ser maior que zero.")
        int quantity,

        @Schema(
                description = "Tipo de movimento: ENTRY (entrada) ou EXIT (saída).",
                example = "ENTRY",
                allowableValues = {"ENTRY", "EXIT"},
                required = true
        )
        @NotNull(message = "O tipo de movimento é obrigatório.")
        MovementType movementType
) {}