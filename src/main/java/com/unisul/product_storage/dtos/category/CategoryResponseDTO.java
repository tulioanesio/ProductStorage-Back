package com.unisul.product_storage.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta detalhada de uma categoria de produto.")
public record CategoryResponseDTO(

        @Schema(description = "Identificador único da categoria.", example = "1")
        Long id,

        @Schema(description = "Nome da categoria.", example = "Eletrônicos")
        String name,

        @Schema(description = "Tamanho ou dimensão padrão dos produtos dessa categoria.", example = "15 polegadas")
        String size,

        @Schema(description = "Tipo de embalagem usada para os produtos dessa categoria.", example = "Caixa")
        String packaging
) {}