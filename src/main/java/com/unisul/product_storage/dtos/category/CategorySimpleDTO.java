package com.unisul.product_storage.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação simplificada de uma categoria, usada em respostas de produtos e relatórios.")
public record CategorySimpleDTO(

        @Schema(description = "Identificador único da categoria.", example = "1")
        Long id,

        @Schema(description = "Nome da categoria.", example = "Informática")
        String name
) {}