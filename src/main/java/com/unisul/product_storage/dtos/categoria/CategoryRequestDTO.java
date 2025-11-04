package com.unisul.product_storage.dtos.categoria;

public record CategoriaRequestDTO(
        String nome,
        String tamanho,
        String embalagem
) {}