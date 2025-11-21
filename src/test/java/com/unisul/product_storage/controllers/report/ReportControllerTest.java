package com.unisul.product_storage.controllers.report;

import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.most_movement_product.MostProductMovementResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.dtos.report.products_by_category.ProductsByCategoryResponseDTO;
import com.unisul.product_storage.services.ReportService;
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
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private ReportService service;

    @InjectMocks
    private ReportController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPriceList() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<PriceListResponseDTO> page = new PageImpl<>(List.of(new PriceListResponseDTO("Notebook", null, "unidade", null)));
        when(service.getPriceList(pageable)).thenReturn(page);

        ResponseEntity<Page<PriceListResponseDTO>> result = controller.getPriceList(pageable);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).hasSize(1);
        verify(service).getPriceList(pageable);
    }

    @Test
    void shouldReturnInventoryBalance() {
        InventoryBalanceResponseDTO response = new InventoryBalanceResponseDTO(null, null);
        when(service.getInventoryBalance(any())).thenReturn(response);

        ResponseEntity<InventoryBalanceResponseDTO> result = controller.getInventoryBalance(Pageable.unpaged());

        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    void shouldReturnLowStockProducts() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<LowStockProductsResponseDTO> page = new PageImpl<>(List.of(new LowStockProductsResponseDTO("Caneta", 3, 10)));
        when(service.getLowStockProducts(pageable)).thenReturn(page);

        ResponseEntity<Page<LowStockProductsResponseDTO>> result = controller.getLowStockProducts(pageable);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).hasSize(1);
    }

    @Test
    void shouldReturnProductsByCategory() {
        Pageable pageable = PageRequest.of(0, 20);
        ProductsByCategoryResponseDTO dto = new ProductsByCategoryResponseDTO("Eletrônicos", 10);
        Page<ProductsByCategoryResponseDTO> page = new PageImpl<>(List.of(dto));
        Long categoryId = null;

        when(service.getProductsByCategory(pageable, categoryId)).thenReturn(page);

        ResponseEntity<Page<ProductsByCategoryResponseDTO>> result = controller.getProductsByCategory(pageable, categoryId);

        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getTotalElements()).isEqualTo(1);
        assertThat(result.getBody().getContent().get(0).name()).isEqualTo("Eletrônicos");
        verify(service).getProductsByCategory(pageable, categoryId);
    }

    @Test
    void shouldReturnMostOutputProduct() {
        List<MostProductMovementResponseDTO> list = List.of(new MostProductMovementResponseDTO("Notebook", 15));
        when(service.getMostOutputProduct()).thenReturn(list);

        ResponseEntity<List<MostProductMovementResponseDTO>> result = controller.getMostOutputProduct();

        assertThat(result.getBody()).hasSize(1);
    }

    @Test
    void shouldReturnMostInputProduct() {
        List<MostProductMovementResponseDTO> list = List.of(new MostProductMovementResponseDTO("Mouse", 20));
        when(service.getMostInputProduct()).thenReturn(list);

        ResponseEntity<List<MostProductMovementResponseDTO>> result = controller.getMostInputProduct();

        assertThat(result.getBody()).hasSize(1);
    }
}