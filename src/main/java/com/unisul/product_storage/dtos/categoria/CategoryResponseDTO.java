package com.unisul.product_storage.dtos.categoria;

public record CategoryResponseDTO(
        Long id,
        String nome,
        String tamanho,
        String embalagem
) {}
