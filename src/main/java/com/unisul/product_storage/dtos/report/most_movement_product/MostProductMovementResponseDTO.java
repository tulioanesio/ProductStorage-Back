package com.unisul.product_storage.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO representando o produto com maior entrada ou saída no estoque")
public record TopProductMovementDTO(

        @Schema(description = "Nome do produto", example = "Notebook Dell Inspiron")
        @NotBlank(message = "O nome do produto não pode estar vazio")
        String productName,

        @Schema(description = "Quantidade total movimentada", example = "120")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        int totalQuantity
) {}
