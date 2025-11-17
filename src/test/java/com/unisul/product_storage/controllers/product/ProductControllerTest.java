package com.unisul.product_storage.controllers.product;

import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.services.ProductService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequestDTO request = new ProductRequestDTO("Notebook", BigDecimal.valueOf(3000), "unidade", 10, 2, 20, 1L);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Notebook", BigDecimal.valueOf(3000), "unidade", 10, 2, 20, null);

        when(service.createProduct(request)).thenReturn(response);

        ResponseEntity<ProductResponseDTO> result = controller.create(request);

        assertThat(result.getStatusCodeValue()).isEqualTo(201);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldGetAllProducts() {
        String name = null;
        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductResponseDTO> page =
                new PageImpl<>(List.of(new ProductResponseDTO(1L, "Notebook",
                        BigDecimal.valueOf(3000), "unidade", 10, 2, 20, null)));

        when(service.getAllProducts(name, pageable)).thenReturn(page);

        ResponseEntity<Page<ProductResponseDTO>> result = controller.getAll(name, pageable);

        assertThat(result.getBody().getContent()).hasSize(1);
    }

    @Test
    void shouldGetAllProductsWithNameFilter() {
        String name = "note";
        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductResponseDTO> page =
                new PageImpl<>(List.of(new ProductResponseDTO(1L, "Notebook",
                        BigDecimal.valueOf(3000), "unidade", 10, 2, 20, null)));

        when(service.getAllProducts(name, pageable)).thenReturn(page);

        ResponseEntity<Page<ProductResponseDTO>> result = controller.getAll(name, pageable);

        assertThat(result.getBody().getContent()).hasSize(1);
        verify(service).getAllProducts(name, pageable);
    }

    @Test
    void shouldGetProductById() {
        ProductResponseDTO response =
                new ProductResponseDTO(1L, "Notebook", BigDecimal.valueOf(3000),
                        "unidade", 10, 2, 20, null);

        when(service.getProductById(1L)).thenReturn(response);

        ResponseEntity<ProductResponseDTO> result = controller.getById(1L);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldUpdateProduct() {
        ProductRequestDTO request =
                new ProductRequestDTO("Mouse", BigDecimal.valueOf(100), "unidade", 50, 5, 100, 1L);

        ProductResponseDTO response =
                new ProductResponseDTO(1L, "Mouse", BigDecimal.valueOf(100), "unidade", 50, 5, 100, null);

        when(service.updateProduct(1L, request)).thenReturn(response);

        ResponseEntity<ProductResponseDTO> result = controller.update(1L, request);

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldDeleteProduct() {
        ResponseEntity<Void> result = controller.delete(1L);

        assertThat(result.getStatusCodeValue()).isEqualTo(204);
        verify(service).deleteProduct(1L);
    }
}
