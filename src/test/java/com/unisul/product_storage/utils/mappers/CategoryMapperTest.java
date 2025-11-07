package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.category.CategoryRequestDTO;
import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;

    @BeforeEach
    void setup() {
        category = new Category();
        category.setId(1L);
        category.setName("Eletrônicos");
        category.setSize("Grande");
        category.setPackaging("Caixa");

        categoryRequestDTO = new CategoryRequestDTO(
                "Eletrônicos",
                "Grande",
                "Caixa"
        );
    }

    @Nested
    @DisplayName("Testes de Mapeamento")
    class MappingTests {

        @Test
        @DisplayName("Deve mapear DTO de Request para Entidade")
        void toEntity_ShouldMapDtoToEntity() {
            Category result = CategoryMapper.toEntity(categoryRequestDTO);

            assertNotNull(result);
            assertNull(result.getId());
            assertEquals("Eletrônicos", result.getName());
            assertEquals("Grande", result.getSize());
            assertEquals("Caixa", result.getPackaging());
        }

        @Test
        @DisplayName("Deve mapear Entidade para DTO de Response")
        void toResponse_ShouldMapEntityToDto() {
            CategoryResponseDTO result = CategoryMapper.toResponse(category);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Eletrônicos", result.name());
            assertEquals("Grande", result.size());
            assertEquals("Caixa", result.packaging());
        }

        @Test
        @DisplayName("Deve mapear Lista de Entidades para Lista de DTOs de Response")
        void toResponseList_ShouldMapEntityListToDtoList() {
            List<Category> entities = List.of(category);
            List<CategoryResponseDTO> result = CategoryMapper.toResponseList(entities);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).id());
            assertEquals("Eletrônicos", result.get(0).name());
        }

        @Test
        @DisplayName("Deve retornar lista vazia ao mapear lista vazia")
        void toResponseList_ShouldReturnEmptyList_WhenInputIsEmpty() {
            List<Category> entities = Collections.emptyList();
            List<CategoryResponseDTO> result = CategoryMapper.toResponseList(entities);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Testes de Cobertura")
    class CoverageTests {

        @Test
        @DisplayName("Deve cobrir o construtor privado")
        void testPrivateConstructor() throws Exception {
            Constructor<CategoryMapper> constructor = CategoryMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            assertDoesNotThrow(() -> constructor.newInstance(), "A instanciação via reflexão não deve falhar.");
            assertNotNull(constructor.newInstance());
        }
    }
}