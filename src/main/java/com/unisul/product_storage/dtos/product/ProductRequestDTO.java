package com.unisul.product_storage.dtos.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Requisição para criação de produto contendo nome, preço, unidade de medida, estoque e categoria.")
public record ProductRequestDTO(

        @Schema(
                description = "Nome do produto",
                example = "Notebook Dell Inspiron 15",
                maxLength = 100
        )
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 100, message = "O nome do produto não deve ultrapassar 100 caracteres.")
        String name,

        @Schema(
                description = "Preço unitário do produto em reais (deve ser maior que zero)",
                example = "4299.99",
                minimum = "0.01"
        )
        @NotNull(message = "O preço unitário é obrigatório.")
        @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero.")
        BigDecimal unitPrice,

        @Schema(
                description = "Unidade de medida do produto (ex: unidade, kg, caixa)",
                example = "unidade",
                maxLength = 10
        )
        @NotBlank(message = "A unidade de medida é obrigatória.")
        @Size(max = 10, message = "A unidade de medida não deve ultrapassar 10 caracteres.")
        String unitOfMeasure,

        @Schema(
                description = "Quantidade disponível em estoque",
                example = "25",
                minimum = "0"
        )
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
        int availableStock,

        @Schema(
                description = "Quantidade mínima permitida em estoque antes de alerta de reposição",
                example = "5",
                minimum = "0"
        )
        @Min(value = 0, message = "A quantidade mínima em estoque não pode ser negativa.")
        int minStockQuantity,

        @Schema(
                description = "Quantidade máxima permitida em estoque",
                example = "100",
                minimum = "1"
        )
        @Min(value = 1, message = "A quantidade máxima em estoque deve ser pelo menos 1.")
        int maxStockQuantity,

        @Schema(
                description = "ID da categoria à qual o produto pertence",
                example = "3"
        )
        Long categoryId
) {
}