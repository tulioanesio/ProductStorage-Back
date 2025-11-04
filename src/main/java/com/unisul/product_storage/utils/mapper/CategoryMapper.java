package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.categoria.CategoryRequestDTO;
import com.unisul.product_storage.dtos.categoria.CategoryResponseDTO;
import com.unisul.product_storage.models.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryRequestDTO request) {
        return new Category(request.nome(), request.tamanho(), request.embalagem());
    }

    public static CategoryResponseDTO toResponse(Category entity) {
        return new CategoryResponseDTO(entity.getId(), entity.getNome(), entity.getTamanho(), entity.getEmbalagem());
    }

    public static List<CategoryResponseDTO> toResponseList(List<Category> entities) {
        return entities.stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
