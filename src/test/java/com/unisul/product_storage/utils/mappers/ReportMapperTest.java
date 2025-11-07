package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ReportMapperTest {

    private ReportMapper reportMapper;
    private Product productWithCategory;
    private Product productWithoutCategory;
    private Category category;

    @BeforeEach
    void setup() {
        reportMapper = new ReportMapper();

        category = new Category();
        category.setId(1L);
        category.setName("Eletrônicos");

        productWithCategory = new Product();
        productWithCategory.setName("Notebook");
        productWithCategory.setUnitPrice(BigDecimal.valueOf(3000.00));
        productWithCategory.setUnitOfMeasure("unidade");
        productWithCategory.setStockAvailable(10);
        productWithCategory.setMinStockQuantity(5);
        productWithCategory.setCategory(category);

        productWithoutCategory = new Product();
        productWithoutCategory.setName("Mouse");
        productWithoutCategory.setUnitPrice(BigDecimal.valueOf(150.00));
        productWithoutCategory.setUnitOfMeasure("pç");
        productWithoutCategory.setStockAvailable(20);
        productWithoutCategory.setMinStockQuantity(10);
        productWithoutCategory.setCategory(null);
    }

    @Nested
    @DisplayName("Testes do 'toPriceListDTO'")
    class ToPriceListDTOTests {

        @Test
        @DisplayName("Deve mapear para PriceListDTO com Categoria")
        void toPriceListDTO_ShouldMap_WithCategory() {
            PriceListResponseDTO result = reportMapper.toPriceListDTO(productWithCategory);

            assertNotNull(result);
            assertEquals("Notebook", result.name());
            assertEquals(BigDecimal.valueOf(3000.00), result.unitPrice());
            assertEquals("unidade", result.unitOfMeasure());

            CategorySimpleDTO categoryDTO = result.category();
            assertNotNull(categoryDTO);
            assertEquals(1L, categoryDTO.id());
            assertEquals("Eletrônicos", categoryDTO.name());
        }

        @Test
        @DisplayName("Deve mapear para PriceListDTO com Categoria nula")
        void toPriceListDTO_ShouldMap_WithNullCategory() {
            PriceListResponseDTO result = reportMapper.toPriceListDTO(productWithoutCategory);

            assertNotNull(result);
            assertEquals("Mouse", result.name());
            assertEquals(BigDecimal.valueOf(150.00), result.unitPrice());

            CategorySimpleDTO categoryDTO = result.category();
            assertNotNull(categoryDTO);
            assertNull(categoryDTO.id());
            assertEquals("Sem categoria", categoryDTO.name());
        }
    }

    @Nested
    @DisplayName("Testes do 'toInventoryBalanceDTO'")
    class ToInventoryBalanceDTOTests {

        @Test
        @DisplayName("Deve mapear para InventoryBalanceDTO e calcular valor total")
        void toInventoryBalanceDTO_ShouldMapAndCalculateTotalValue() {
            InventoryBalanceDTO result = reportMapper.toInventoryBalanceDTO(productWithCategory);

            assertNotNull(result);
            assertEquals("Notebook", result.name());
            assertEquals(10, result.stockAvailable());
            assertEquals(0, BigDecimal.valueOf(30000.00).compareTo(result.totalValue()));
        }
    }

    @Nested
    @DisplayName("Testes do 'toLowStockProductsDTO'")
    class ToLowStockProductsDTOTests {

        @Test
        @DisplayName("Deve mapear para LowStockProductsResponseDTO")
        void toLowStockProductsDTO_ShouldMapCorrectly() {
            LowStockProductsResponseDTO result = reportMapper.toLowStockProductsDTO(productWithCategory);

            assertNotNull(result);
            assertEquals("Notebook", result.name());
            assertEquals(10, result.quantity());
            assertEquals(5, result.minStockQuantity());
        }
    }
}