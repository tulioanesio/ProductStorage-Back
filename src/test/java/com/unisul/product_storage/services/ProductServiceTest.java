package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.CategoryRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mappers.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService service;

    private Product product;
    private Category category;
    private ProductRequestDTO productRequestDTO;
    private ProductResponseDTO productResponseDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Eletrônicos");
        category.setSize("Grande");
        category.setPackaging("Caixa");

        product = new Product();
        product.setId(1L);
        product.setName("Notebook Dell");
        product.setUnitPrice(BigDecimal.valueOf(4299.99));
        product.setStockAvailable(25);
        product.setMinStockQuantity(5);
        product.setMaxStockQuantity(100);
        product.setUnitOfMeasure("unidade");
        product.setCategory(category);

        productRequestDTO = new ProductRequestDTO(
                "Notebook Dell",
                BigDecimal.valueOf(4299.99),
                "unidade",
                25,
                5,
                100,
                1L
        );

        categoryResponseDTO = new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getSize(),
                category.getPackaging()
        );

        productResponseDTO = new ProductResponseDTO(
                1L,
                "Notebook Dell",
                BigDecimal.valueOf(4299.99),
                "unidade",
                25,
                5,
                100,
                categoryResponseDTO
        );
    }

    @Nested
    @DisplayName("Testes de Criação (Create)")
    class CreateProductTests {

        @Test
        @DisplayName("Deve lançar BusinessException se o nome for nulo")
        void createProduct_ShouldThrow_WhenNameIsNull() {
            ProductRequestDTO dto = new ProductRequestDTO(
                    null,
                    BigDecimal.valueOf(10.0), "un", 10, 1, 20, 1L
            );
            assertThrows(BusinessException.class, () -> service.createProduct(dto));
        }

        @Test
        @DisplayName("Deve lançar BusinessException se o nome estiver em branco")
        void createProduct_ShouldThrow_WhenNameIsBlank() {
            ProductRequestDTO dto = new ProductRequestDTO(
                    " ",
                    BigDecimal.valueOf(4299.99), "unidade", 25, 5, 100, 1L
            );
            assertThrows(BusinessException.class, () -> service.createProduct(dto));
        }

        @Test
        @DisplayName("Deve lançar BusinessException se a categoria não for encontrada")
        void createProduct_ShouldThrow_WhenCategoryNotFound() {
            ProductRequestDTO dto = new ProductRequestDTO(
                    "Notebook Dell",
                    BigDecimal.valueOf(4299.99), "unidade", 25, 5, 100, 999L
            );

            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.createProduct(dto));
        }

        @Test
        @DisplayName("Deve salvar produto com categoria válida")
        void createProduct_ShouldSave_WhenValidWithCategory() {
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            when(mapper.toEntity(productRequestDTO, category)).thenReturn(product);
            when(productRepository.save(product)).thenReturn(product);
            when(mapper.toResponseDTO(product)).thenReturn(productResponseDTO);

            ProductResponseDTO result = service.createProduct(productRequestDTO);

            assertNotNull(result);
            assertEquals("Notebook Dell", result.name());
            assertEquals(1L, result.category().id());
            verify(productRepository, times(1)).save(product);
        }

        @Test
        @DisplayName("Deve salvar produto com categoria nula (sem categoria)")
        void createProduct_ShouldSave_WhenValidWithNullCategory() {
            ProductRequestDTO dtoNullCategory = new ProductRequestDTO(
                    "Produto Sem Categoria",
                    BigDecimal.valueOf(50.0), "un", 10, 1, 20, null
            );
            Product productNoCategory = new Product();
            productNoCategory.setId(2L);
            productNoCategory.setName("Produto Sem Categoria");

            ProductResponseDTO responseNoCategory = new ProductResponseDTO(
                    2L, "Produto Sem Categoria",
                    BigDecimal.valueOf(50.0), "un", 10, 1, 20, null
            );

            when(mapper.toEntity(dtoNullCategory, null)).thenReturn(productNoCategory);
            when(productRepository.save(productNoCategory)).thenReturn(productNoCategory);
            when(mapper.toResponseDTO(productNoCategory)).thenReturn(responseNoCategory);

            ProductResponseDTO result = service.createProduct(dtoNullCategory);

            assertNotNull(result);
            assertEquals("Produto Sem Categoria", result.name());
            assertNull(result.category());
            verify(categoryRepository, never()).findById(any());
            verify(productRepository, times(1)).save(productNoCategory);
        }
    }

    @Nested
    @DisplayName("Testes de Leitura (Read)")
    class ReadProductTests {

        @Test
        @DisplayName("Deve retornar página de produtos")
        void getAllProducts_ShouldReturnPage() {
            Page<Product> page = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);
            when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
            when(mapper.toResponseDTO(product)).thenReturn(productResponseDTO);

            Page<ProductResponseDTO> result = service.getAllProducts(PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
            assertEquals("Notebook Dell", result.getContent().get(0).name());
        }

        @Test
        @DisplayName("Deve lançar BusinessException ao buscar ID inexistente")
        void getProductById_ShouldThrow_WhenNotFound() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.getProductById(99L));
        }

        @Test
        @DisplayName("Deve retornar DTO quando produto é encontrado")
        void getProductById_ShouldReturnDto_WhenFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(mapper.toResponseDTO(product)).thenReturn(productResponseDTO);

            ProductResponseDTO result = service.getProductById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Notebook Dell", result.name());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateProductTests {

        @Test
        @DisplayName("Deve lançar BusinessException ao atualizar produto inexistente")
        void updateProduct_ShouldThrow_WhenProductNotFound() {
            when(productRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.updateProduct(99L, productRequestDTO));
        }

        @Test
        @DisplayName("Deve lançar BusinessException ao atualizar com categoria inexistente")
        void updateProduct_ShouldThrow_WhenCategoryNotFound() {
            ProductRequestDTO dtoWithBadCategory = new ProductRequestDTO(
                    "Nome Novo",
                    BigDecimal.valueOf(1.0), "un", 1, 1, 1, 999L
            );

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> service.updateProduct(1L, dtoWithBadCategory));
        }

        @Test
        @DisplayName("Deve atualizar produto com categoria válida")
        void updateProduct_ShouldUpdate_WhenValidWithCategory() {
            ProductRequestDTO dtoUpdate = new ProductRequestDTO(
                    "Nome Atualizado",
                    BigDecimal.valueOf(1.0), "un", 1, 1, 1, 1L
            );

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
            doNothing().when(mapper).updateEntity(product, dtoUpdate, category);
            when(productRepository.save(product)).thenReturn(product);
            when(mapper.toResponseDTO(product)).thenReturn(productResponseDTO);

            ProductResponseDTO result = service.updateProduct(1L, dtoUpdate);

            assertNotNull(result);
            verify(mapper, times(1)).updateEntity(product, dtoUpdate, category);
            verify(productRepository, times(1)).save(product);
        }

        @Test
        @DisplayName("Deve atualizar produto com categoria nula (remover categoria)")
        void updateProduct_ShouldUpdate_WhenValidWithNullCategory() {
            ProductRequestDTO dtoNullCategory = new ProductRequestDTO(
                    "Nome Atualizado",
                    BigDecimal.valueOf(1.0), "un", 1, 1, 1, null
            );

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            doNothing().when(mapper).updateEntity(product, dtoNullCategory, null);
            when(productRepository.save(product)).thenReturn(product);
            when(mapper.toResponseDTO(product)).thenReturn(productResponseDTO);

            ProductResponseDTO result = service.updateProduct(1L, dtoNullCategory);

            assertNotNull(result);
            verify(categoryRepository, never()).findById(any());
            verify(mapper, times(1)).updateEntity(product, dtoNullCategory, null);
            verify(productRepository, times(1)).save(product);
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão (Delete)")
    class DeleteProductTests {

        @Test
        @DisplayName("Deve lançar BusinessException ao excluir produto inexistente")
        void deleteProduct_ShouldThrow_WhenProductNotFound() {
            when(productRepository.existsById(99L)).thenReturn(false);
            assertThrows(BusinessException.class, () -> service.deleteProduct(99L));
        }

        @Test
        @DisplayName("Deve chamar deleteById quando produto existir")
        void deleteProduct_ShouldDelete_WhenProductExists() {
            when(productRepository.existsById(1L)).thenReturn(true);
            doNothing().when(productRepository).deleteById(1L);

            assertDoesNotThrow(() -> service.deleteProduct(1L));
            verify(productRepository, times(1)).existsById(1L);
            verify(productRepository, times(1)).deleteById(1L);
        }
    }
}