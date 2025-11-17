package com.unisul.product_storage.controllers.category;

import com.unisul.product_storage.dtos.category.CategoryRequestDTO;
import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO("Eletrônicos", "15 polegadas", "Caixa");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Eletrônicos", "15 polegadas", "Caixa");

        when(categoryService.createCategory(request)).thenReturn(response);

        ResponseEntity<CategoryResponseDTO> result = controller.createCategory(request);

        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(response);
        verify(categoryService).createCategory(request);
    }

    @Test
    void shouldGetAllCategories() {
        String name = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<CategoryResponseDTO> page = new PageImpl<>(
                List.of(new CategoryResponseDTO(1L, "Eletrônicos", "15 polegadas", "Caixa"))
        );

        when(categoryService.getAllCategories(name, pageable)).thenReturn(page);

        ResponseEntity<Page<CategoryResponseDTO>> result = controller.getAllCategories(name, pageable);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody().getContent()).hasSize(1);

        verify(categoryService).getAllCategories(name, pageable);
    }

    @Test
    void shouldGetAllCategoriesWithNameFilter() {
        String name = "ele";
        Pageable pageable = PageRequest.of(0, 10);

        Page<CategoryResponseDTO> page = new PageImpl<>(
                List.of(new CategoryResponseDTO(1L, "Eletrônicos", "15", "Caixa"))
        );

        when(categoryService.getAllCategories(name, pageable)).thenReturn(page);

        ResponseEntity<Page<CategoryResponseDTO>> result = controller.getAllCategories(name, pageable);

        assertThat(result.getBody().getContent()).hasSize(1);
        verify(categoryService).getAllCategories(name, pageable);
    }

    @Test
    void shouldGetCategoryById() {
        CategoryResponseDTO response =
                new CategoryResponseDTO(1L, "Eletrônicos", "15 polegadas", "Caixa");

        when(categoryService.getCategoryById(1L)).thenReturn(response);

        ResponseEntity<CategoryResponseDTO> result = controller.getCategoryById(1L);

        assertThat(result.getBody()).isEqualTo(response);
        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void shouldUpdateCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO("Eletrônicos", "15", "Caixa");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Eletrônicos", "15", "Caixa");

        when(categoryService.updateCategory(1L, request)).thenReturn(response);

        ResponseEntity<CategoryResponseDTO> result = controller.updateCategory(1L, request);

        assertThat(result.getBody()).isEqualTo(response);
        verify(categoryService).updateCategory(1L, request);
    }

    @Test
    void shouldDeleteCategory() {
        ResponseEntity<Void> result = controller.deleteCategory(1L);

        assertThat(result.getStatusCodeValue()).isEqualTo(204);
        verify(categoryService).deleteCategory(1L);
    }
}
