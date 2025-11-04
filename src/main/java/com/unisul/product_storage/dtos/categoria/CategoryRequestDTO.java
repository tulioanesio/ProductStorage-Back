package com.unisul.product_storage.dtos.categoria;

public record CategoryRequestDTO(
        String nome,
        String tamanho,
        String embalagem
) {}