package com.unisul.product_storage.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para criação de uma nova category de produto.")
public record CategoryRequestDTO(

        @Schema(
                description = "Nome da category do produto.",
                example = "Eletrônicos",
                maxLength = 100,
                required = true
        )
        @NotBlank(message = "O nome da category é obrigatório.")
        @Size(max = 100, message = "O nome da category não pode exceder 100 caracteres.")
        String name,

        @Schema(
                description = "Tamanho ou dimensão padrão dos produtos dessa category.",
                example = "15 polegadas",
                maxLength = 20
        )
        @Size(max = 20, message = "O tamanho não pode exceder 20 caracteres.")
        String size,

        @Schema(
                description = "Tipo de embalagem usada para os produtos dessa category.",
                example = "Caixa",
                maxLength = 20
        )
        @Size(max = 20, message = "A embalagem não pode exceder 20 caracteres.")
        String packaging
) {}