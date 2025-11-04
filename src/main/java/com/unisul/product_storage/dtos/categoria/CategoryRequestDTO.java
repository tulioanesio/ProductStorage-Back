package com.unisul.product_storage.dtos.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(max = 100, message = "Nome da categoria não pode exceder 100 caracteres")
        String nome,

        @Size(max = 20, message = "Tamanho não pode exceder 20 caracteres")
        String tamanho,

        @Size(max = 20, message = "Embalagem não pode exceder 20 caracteres")
        String embalagem
) {}
