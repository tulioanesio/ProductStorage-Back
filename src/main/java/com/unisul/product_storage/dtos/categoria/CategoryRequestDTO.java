package com.unisul.product_storage.dtos.categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para criação de uma nova categoria de produto.")
public record CategoryRequestDTO(

        @Schema(
                description = "Nome da categoria do produto.",
                example = "Eletrônicos",
                maxLength = 100,
                required = true
        )
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 100, message = "O nome da categoria não pode exceder 100 caracteres.")
        String name,

        @Schema(
                description = "Tamanho ou dimensão padrão dos produtos dessa categoria.",
                example = "15 polegadas",
                maxLength = 20
        )
        @Size(max = 20, message = "O tamanho não pode exceder 20 caracteres.")
        String size,

        @Schema(
                description = "Tipo de embalagem usada para os produtos dessa categoria.",
                example = "Caixa",
                maxLength = 20
        )
        @Size(max = 20, message = "A embalagem não pode exceder 20 caracteres.")
        String packaging
) {}