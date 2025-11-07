package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.most_movement_product.MostProductMovementResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.dtos.report.products_by_category.ProductsByCategoryResponseDTO;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mappers.ReportMapper;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportService reportService;

    private Product product;
    private Pageable pageable;
    private Page<Product> productPage;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setUnitPrice(BigDecimal.valueOf(3000.00));
        product.setStockAvailable(10);
        product.setMinStockQuantity(15);
        product.setUnitOfMeasure("unidade");

        pageable = PageRequest.of(0, 10);
        productPage = new PageImpl<>(List.of(product), pageable, 1);
    }

    @Nested
    @DisplayName("Relatório: Lista de Preços")
    class PriceListReport {
        @Test
        @DisplayName("Deve retornar a lista de preços paginada")
        void getPriceList_ShouldReturnPage() {
            PriceListResponseDTO dto = new PriceListResponseDTO(
                    "Notebook",
                    BigDecimal.valueOf(3000.00),
                    "unidade",
                    null
            );

            when(productRepository.findAll(pageable)).thenReturn(productPage);
            when(reportMapper.toPriceListDTO(any(Product.class))).thenReturn(dto);

            Page<PriceListResponseDTO> result = reportService.getPriceList(pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals("Notebook", result.getContent().get(0).name());
            assertEquals("unidade", result.getContent().get(0).unitOfMeasure());
        }
    }

    @Nested
    @DisplayName("Relatório: Balanço de Estoque")
    class InventoryBalanceReport {

        @Test
        @DisplayName("Deve retornar o balanço de estoque com valor total")
        void getInventoryBalance_ShouldReturnCalculatedValues() {
            InventoryBalanceDTO itemDto = new InventoryBalanceDTO(
                    "Notebook",
                    10,
                    BigDecimal.valueOf(30000.00)
            );
            BigDecimal totalValue = BigDecimal.valueOf(30000.00);

            when(productRepository.findAll(pageable)).thenReturn(productPage);
            when(reportMapper.toInventoryBalanceDTO(any(Product.class))).thenReturn(itemDto);
            when(productRepository.calculateTotalStockValue()).thenReturn(totalValue);

            InventoryBalanceResponseDTO result = reportService.getInventoryBalance(pageable);

            assertNotNull(result);
            assertEquals(totalValue, result.stockValue());
            assertEquals(1, result.items().getTotalElements());
            assertEquals("Notebook", result.items().getContent().get(0).name());
            assertEquals(10, result.items().getContent().get(0).stockAvailable());
        }

        @Test
        @DisplayName("Deve retornar valor zero quando não há produtos")
        void getInventoryBalance_ShouldReturnZero_WhenNoProducts() {
            when(productRepository.findAll(pageable)).thenReturn(Page.empty(pageable));
            when(productRepository.calculateTotalStockValue()).thenReturn(BigDecimal.ZERO);

            InventoryBalanceResponseDTO result = reportService.getInventoryBalance(pageable);

            assertNotNull(result);
            assertEquals(BigDecimal.ZERO, result.stockValue());
            assertTrue(result.items().isEmpty());
        }

        @Test
        @DisplayName("Deve lidar com valor nulo do repositório")
        void getInventoryBalance_ShouldHandleNullTotalValue() {
            when(productRepository.findAll(pageable)).thenReturn(Page.empty(pageable));
            when(productRepository.calculateTotalStockValue()).thenReturn(null);

            InventoryBalanceResponseDTO result = reportService.getInventoryBalance(pageable);

            assertNotNull(result);
            assertNull(result.stockValue());
            assertTrue(result.items().isEmpty());
        }
    }

    @Nested
    @DisplayName("Relatório: Produtos com Baixo Estoque")
    class LowStockReport {
        @Test
        @DisplayName("Deve retornar produtos com baixo estoque paginados")
        void getLowStockProducts_ShouldReturnPage() {
            LowStockProductsResponseDTO dto = new LowStockProductsResponseDTO("Notebook", 10, 15);

            when(productRepository.findLowStockProducts(pageable)).thenReturn(productPage);
            when(reportMapper.toLowStockProductsDTO(any(Product.class))).thenReturn(dto);

            Page<LowStockProductsResponseDTO> result = reportService.getLowStockProducts(pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals("Notebook", result.getContent().get(0).name());
            assertEquals(10, result.getContent().get(0).quantity());
            assertEquals(15, result.getContent().get(0).minStockQuantity());
        }
    }

    @Nested
    @DisplayName("Relatório: Produtos por Categoria")
    class ProductsByCategoryReport {

        @Test
        @DisplayName("Deve retornar contagem de produtos para uma categoria específica")
        void getProductsByCategory_ShouldReturn_WhenCategoryIdIsProvided() {
            Object[] row = new Object[]{"Eletrônicos", 10L};
            List<Object[]> results = Collections.singletonList(row);

            when(productRepository.countProductsByCategoryId(1L)).thenReturn(results);

            List<ProductsByCategoryResponseDTO> resultList = reportService.getProductsByCategory(1L);

            assertEquals(1, resultList.size());
            assertEquals("Eletrônicos", resultList.get(0).name());
            assertEquals(10, resultList.get(0).quantity());
        }

        @Test
        @DisplayName("Deve retornar contagem de produtos para todas as categorias")
        void getProductsByCategory_ShouldReturn_WhenCategoryIdIsNull() {
            Object[] row1 = new Object[]{"Eletrônicos", 10L};
            Object[] row2 = new Object[]{"Móveis", 5};
            List<Object[]> results = List.of(row1, row2);

            when(productRepository.countProductsByCategory()).thenReturn(results);

            List<ProductsByCategoryResponseDTO> resultList = reportService.getProductsByCategory(null);

            assertEquals(2, resultList.size());
            assertEquals("Eletrônicos", resultList.get(0).name());
            assertEquals(10, resultList.get(0).quantity());
            assertEquals("Móveis", resultList.get(1).name());
            assertEquals(5, resultList.get(1).quantity());
        }

        @Test
        @DisplayName("Deve retornar lista vazia se a consulta retornar nulo")
        void getProductsByCategory_ShouldReturnEmptyList_WhenResultsAreNull() {
            when(productRepository.countProductsByCategory()).thenReturn(null);
            List<ProductsByCategoryResponseDTO> resultList = reportService.getProductsByCategory(null);
            assertTrue(resultList.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar lista vazia se a consulta retornar lista vazia")
        void getProductsByCategory_ShouldReturnEmptyList_WhenResultsAreEmpty() {
            when(productRepository.countProductsByCategoryId(1L)).thenReturn(Collections.emptyList());
            List<ProductsByCategoryResponseDTO> resultList = reportService.getProductsByCategory(1L);
            assertTrue(resultList.isEmpty());
        }
    }

    @Nested
    @DisplayName("Relatório: Produtos Mais Movimentados")
    class MostMovedProductsReport {

        @Test
        @DisplayName("Deve retornar produtos com mais SAÍDAS")
        void getMostOutputProduct_ShouldReturnList() {
            Object[] row = new Object[]{"Notebook", 50L};
            List<Object[]> results = Collections.singletonList(row);

            when(movementRepository.findTopProductBySaida()).thenReturn(results);

            List<MostProductMovementResponseDTO> resultList = reportService.getMostOutputProduct();

            assertEquals(1, resultList.size());
            assertEquals("Notebook", resultList.get(0).productName());
            assertEquals(50, resultList.get(0).totalQuantity());
        }

        @Test
        @DisplayName("Deve retornar lista vazia se não houver SAÍDAS")
        void getMostOutputProduct_ShouldReturnEmptyList() {
            when(movementRepository.findTopProductBySaida()).thenReturn(Collections.emptyList());
            List<MostProductMovementResponseDTO> resultList = reportService.getMostOutputProduct();
            assertTrue(resultList.isEmpty());
        }

        @Test
        @DisplayName("Deve retornar produtos com mais ENTRADAS")
        void getMostInputProduct_ShouldReturnList() {
            Object[] row = new Object[]{"Mouse", 100};
            List<Object[]> results = Collections.singletonList(row);

            when(movementRepository.findTopProductByEntrada()).thenReturn(results);

            List<MostProductMovementResponseDTO> resultList = reportService.getMostInputProduct();

            assertEquals(1, resultList.size());
            assertEquals("Mouse", resultList.get(0).productName());
            assertEquals(100, resultList.get(0).totalQuantity());
        }

        @Test
        @DisplayName("Deve retornar lista vazia se não houver ENTRADAS")
        void getMostInputProduct_ShouldReturnEmptyList() {
            when(movementRepository.findTopProductByEntrada()).thenReturn(Collections.emptyList());
            List<MostProductMovementResponseDTO> resultList = reportService.getMostInputProduct();
            assertTrue(resultList.isEmpty());
        }
    }
}