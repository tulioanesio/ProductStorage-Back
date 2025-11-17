package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.category.CategoryRequestDTO;
import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Eletrônicos");
        category.setSize("Grande");
        category.setPackaging("Caixa");
    }

    @Nested
    @DisplayName("Testes de Criação (Create)")
    class CreateCategoryTests {

        @Test
        @DisplayName("Deve salvar e retornar DTO ao criar com dados válidos")
        void createCategory_ShouldSaveAndReturnDTO() {
            CategoryRequestDTO dto = new CategoryRequestDTO("Eletrônicos", "Grande", "Caixa");
            when(repository.save(any(Category.class))).thenReturn(category);

            CategoryResponseDTO result = service.createCategory(dto);

            assertNotNull(result);
            assertEquals("Eletrônicos", result.name());
            verify(repository, times(1)).save(any(Category.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com nome em branco")
        void createCategory_ShouldThrow_WhenNameIsBlank() {
            CategoryRequestDTO dto = new CategoryRequestDTO(" ", "A", "B");
            BusinessException ex = assertThrows(BusinessException.class, () -> service.createCategory(dto));
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }

        @Test
        @DisplayName("Deve lançar exceção ao criar com nome nulo")
        void createCategory_ShouldThrow_WhenNameIsNull() {
            CategoryRequestDTO dto = new CategoryRequestDTO(null, "Grande", "Caixa");

            BusinessException ex = assertThrows(BusinessException.class, () -> service.createCategory(dto));
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        }
    }

    @Nested
    @DisplayName("Testes de Leitura (Read)")
    class ReadCategoryTests {

        @Test
        @DisplayName("Deve retornar DTOs paginados")
        void getAllCategories_ShouldReturnPagedDTOs() {
            when(repository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(category)));

            var result = service.getAllCategories(null, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
            assertEquals("Eletrônicos", result.getContent().get(0).name());
        }

        @Test
        @DisplayName("Deve retornar DTO ao buscar por ID existente")
        void getCategoryById_ShouldReturn_WhenExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(category));

            var result = service.getCategoryById(1L);

            assertNotNull(result);
            assertEquals("Eletrônicos", result.name());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
        void getCategoryById_ShouldThrow_WhenNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            BusinessException ex = assertThrows(BusinessException.class, () -> service.getCategoryById(99L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateCategoryTests {

        @Test
        @DisplayName("Deve atualizar e retornar DTO ao atualizar categoria existente")
        void updateCategory_ShouldUpdateAndReturnDTO() {
            CategoryRequestDTO dto = new CategoryRequestDTO("Informática", "Médio", "Pacote");
            when(repository.findById(1L)).thenReturn(Optional.of(category));
            when(repository.save(any(Category.class))).thenReturn(category);

            CategoryResponseDTO result = service.updateCategory(1L, dto);

            assertNotNull(result);
            assertEquals("Informática", category.getName());
            assertEquals("Médio", category.getSize());
            assertEquals("Pacote", category.getPackaging());
            verify(repository).save(any(Category.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar categoria inexistente")
        void updateCategory_ShouldThrow_WhenNotFound() {
            CategoryRequestDTO dto = new CategoryRequestDTO("Informática", "Médio", "Pacote");
            when(repository.findById(1L)).thenReturn(Optional.empty());

            BusinessException ex = assertThrows(BusinessException.class, () -> service.updateCategory(1L, dto));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão (Delete)")
    class DeleteCategoryTests {

        @Test
        @DisplayName("Deve lançar exceção ao excluir categoria inexistente")
        void deleteCategory_ShouldThrow_WhenNotExists() {
            when(repository.existsById(1L)).thenReturn(false);
            BusinessException ex = assertThrows(BusinessException.class, () -> service.deleteCategory(1L));
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        }

        @Test
        @DisplayName("Deve excluir categoria quando ela existir")
        void deleteCategory_ShouldDelete_WhenExists() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            service.deleteCategory(1L);

            verify(repository, times(1)).deleteById(1L);
        }
    }
}