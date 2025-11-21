package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.most_movement_product.MostProductMovementResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.dtos.report.products_by_category.ProductsByCategoryResponseDTO;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mappers.ReportMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final MovementRepository movementRepository;
    private final ReportMapper reportMapper;


    public ReportService(ProductRepository productRepository, MovementRepository movementRepository, ReportMapper reportMapper) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
        this.reportMapper = reportMapper;
    }

    public Page<PriceListResponseDTO> getPriceList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(reportMapper::toPriceListDTO);
    }

    public InventoryBalanceResponseDTO getInventoryBalance(Pageable pageable) {
        Page<InventoryBalanceDTO> inventoryPage = productRepository.findAll(pageable)
                .map(reportMapper::toInventoryBalanceDTO);

        BigDecimal stockValue = productRepository.calculateTotalStockValue();

        return new InventoryBalanceResponseDTO(stockValue, inventoryPage);
    }

    public Page<LowStockProductsResponseDTO> getLowStockProducts(Pageable pageable) {
        return productRepository.findLowStockProducts(pageable)
                .map(reportMapper::toLowStockProductsDTO);
    }

    public Page<ProductsByCategoryResponseDTO> getProductsByCategory(Pageable pageable, Long categoryId) {
        if (categoryId != null) {
            List<Object[]> results = productRepository.countProductsByCategoryId(categoryId);

            if (results == null || results.isEmpty()) {
                return Page.empty(pageable);
            }

            List<ProductsByCategoryResponseDTO> dtos = results.stream()
                    .map(result -> new ProductsByCategoryResponseDTO(
                            (String) result[0],
                            ((Number) result[1]).intValue()
                    ))
                    .toList();

            return new PageImpl<>(dtos, pageable, dtos.size());
        } else {
            return productRepository.countProductsByCategory(pageable)
                    .map(result -> new ProductsByCategoryResponseDTO(
                            (String) result[0],
                            ((Number) result[1]).intValue()
                    ));
        }
    }

    public List<MostProductMovementResponseDTO> getMostOutputProduct() {
        List<Object[]> resultList = movementRepository.findTopProductBySaida();

        if (resultList.isEmpty()) {
            return Collections.emptyList();
        }

        return resultList.stream()
                .map(row -> new MostProductMovementResponseDTO(
                        (String) row[0],
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }

    public List<MostProductMovementResponseDTO> getMostInputProduct() {
        List<Object[]> resultList = movementRepository.findTopProductByEntrada();

        if (resultList.isEmpty()) {
            return Collections.emptyList();
        }

        return resultList.stream()
                .map(row -> new MostProductMovementResponseDTO(
                        (String) row[0],
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }
}