package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.models.Categoria;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaMapper {

    private CategoriaMapper() {}

    public static Categoria toEntity(CategoriaRequest request) {
        return new Categoria(request.getNome(), request.getTamanho(), request.getEmbalagem());
    }

    public static CategoriaResponse toResponse(Categoria entity) {
        return new CategoriaResponse(entity.getId(), entity.getNome(), entity.getTamanho(), entity.getEmbalagem());
    }

    public static List<CategoriaResponse> toResponseList(List<Categoria> entities) {
        return entities.stream()
                .map(CategoriaMapper::toResponse)
                .collect(Collectors.toList());
    }
}
