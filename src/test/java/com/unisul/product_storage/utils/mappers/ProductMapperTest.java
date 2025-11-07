package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper;
    private ProductRequestDTO productRequestDTO;
    private Category category;
    private Product product;

    @BeforeEach
    void setup() {
        productMapper = new ProductMapper();

        category = new Category();
        category.setId(1L);
        category.setName("Eletrônicos");
        category.setSize("Grande");
        category.setPackaging("Caixa");

        productRequestDTO = new ProductRequestDTO(
                "Notebook",
                BigDecimal.valueOf(3000.00),
                "unidade",
                10,
                5,
                50,
                1L
        );

        product = new Product();
        product.setId(1L);
        product.setName("Notebook Antigo");
        product.setUnitPrice(BigDecimal.valueOf(1000.00));
        product.setUnitOfMeasure("pç");
        product.setStockAvailable(1);
        product.setMinStockQuantity(1);
        product.setMaxStockQuantity(1);
        product.setCategory(null);
    }

    @Nested
    @DisplayName("Testes do 'toEntity'")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear DTO para Entidade com Categoria")
        void toEntity_ShouldMapDtoToEntity_WithCategory() {
            Product result = productMapper.toEntity(productRequestDTO, category);

            assertNotNull(result);
            assertEquals("Notebook", result.getName());
            assertEquals(10, result.getStockAvailable());
            assertEquals(category, result.getCategory());
        }

        @Test
        @DisplayName("Deve mapear DTO para Entidade com Categoria nula")
        void toEntity_ShouldMapDtoToEntity_WithNullCategory() {
            Product result = productMapper.toEntity(productRequestDTO, null);

            assertNotNull(result);
            assertEquals("Notebook", result.getName());
            assertNull(result.getCategory());
        }
    }

    @Nested
    @DisplayName("Testes do 'updateEntity'")
    class UpdateEntityTests {

        @Test
        @DisplayName("Deve atualizar Entidade com dados do DTO e Categoria")
        void updateEntity_ShouldUpdateEntity_WithCategory() {
            productMapper.updateEntity(product, productRequestDTO, category);

            assertEquals("Notebook", product.getName());
            assertEquals(BigDecimal.valueOf(3000.00), product.getUnitPrice());
            assertEquals("unidade", product.getUnitOfMeasure());
            assertEquals(10, product.getStockAvailable());
            assertEquals(5, product.getMinStockQuantity());
            assertEquals(50, product.getMaxStockQuantity());
            assertEquals(category, product.getCategory());
        }

        @Test
        @DisplayName("Deve atualizar Entidade com Categoria nula")
        void updateEntity_ShouldUpdateEntity_WithNullCategory() {
            product.setCategory(category);
            productMapper.updateEntity(product, productRequestDTO, null);

            assertEquals("Notebook", product.getName());
            assertNull(product.getCategory());
        }
    }

    @Nested
    @DisplayName("Testes do 'toResponseDTO'")
    class ToResponseDTOTests {

        @Test
        @DisplayName("Deve mapear Entidade para DTO com Categoria")
        void toResponseDTO_ShouldMapEntityToDto_WithCategory() {
            product.setCategory(category);

            ProductResponseDTO result = productMapper.toResponseDTO(product);

            assertNotNull(result);
            assertEquals(product.getName(), result.name());
            assertEquals(product.getStockAvailable(), result.availableStock());

            CategoryResponseDTO categoryDTO = result.category();
            assertNotNull(categoryDTO);
            assertEquals(1L, categoryDTO.id());
            assertEquals("Eletrônicos", categoryDTO.name());
        }

        @Test
        @DisplayName("Deve mapear Entidade para DTO com Categoria nula")
        void toResponseDTO_ShouldMapEntityToDto_WithNullCategory() {
            product.setCategory(null);

            ProductResponseDTO result = productMapper.toResponseDTO(product);

            assertNotNull(result);
            assertEquals(product.getName(), result.name());
            assertNull(result.category());
        }
    }
}