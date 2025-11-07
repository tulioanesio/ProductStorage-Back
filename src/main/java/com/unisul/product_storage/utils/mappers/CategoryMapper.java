package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.category.CategoryRequestDTO;
import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.models.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    private CategoryMapper() {}

    public static Category toEntity(CategoryRequestDTO request) {
        return new Category(request.name(), request.size(), request.packaging());
    }

    public static CategoryResponseDTO toResponse(Category entity) {
        return new CategoryResponseDTO(entity.getId(), entity.getName(), entity.getSize(), entity.getPackaging());
    }

    public static List<CategoryResponseDTO> toResponseList(List<Category> entities) {
        return entities.stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
