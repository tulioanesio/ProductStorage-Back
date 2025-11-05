package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.categoria.CategoryRequestDTO;
import com.unisul.product_storage.dtos.categoria.CategoryResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.repositories.CategoryRepository;
import com.unisul.product_storage.utils.mapper.CategoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Nome inválido",
                    "O nome da categoria não pode ser vazio ou nulo."
            );
        }

        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponse(saved);
    }

    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryMapper::toResponse);
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada",
                        "Categoria com ID " + id + " não existe."
                ));
        return CategoryMapper.toResponse(category);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada",
                        "Não foi possível atualizar. ID " + id + " não existe."
                ));

        existing.setName(request.name());
        existing.setSize(request.size());
        existing.setPackaging(request.packaging());

        Category updated = categoryRepository.save(existing);
        return CategoryMapper.toResponse(updated);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "Categoria não encontrada",
                    "Não foi possível excluir. ID " + id + " não existe."
            );
        }
        categoryRepository.deleteById(id);
    }
}
