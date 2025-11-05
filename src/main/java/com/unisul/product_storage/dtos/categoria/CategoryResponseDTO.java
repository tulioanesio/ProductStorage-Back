package com.unisul.product_storage.dtos.categoria;

public record CategoryResponseDTO(
        Long id,
        String name,
        String size,
        String packaging
) {}
