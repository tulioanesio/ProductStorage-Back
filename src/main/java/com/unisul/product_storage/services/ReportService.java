package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.report.*;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mapper.ReportMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final ReportMapper reportMapper;

    public ReportService(ProductRepository productRepository, ReportMapper reportMapper) {
        this.productRepository = productRepository;
        this.reportMapper = reportMapper;
    }

    public Page<PriceListDTO> getPriceList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(reportMapper::toPriceListDTO);
    }

    public InventoryBalanceResponseDTO getInventoryBalance(Pageable pageable) {
        Page<InventoryBalanceDTO> inventoryPage = productRepository.findAll(pageable)
                .map(reportMapper::toInventoryBalanceDTO);


        BigDecimal stockValue = productRepository.findAll().stream()
                .map(product -> product.getUnitPrice()
                        .multiply(BigDecimal.valueOf(product.getStockAvailable())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventoryBalanceResponseDTO(stockValue, inventoryPage);
    }

    public Page<LowStockProductsDTO> getLowStockProducts(Pageable pageable) {
        return productRepository.findLowStockProducts(pageable)
                .map(reportMapper::toLowStockProductsDTO);
    }

    public List<ProductsByCategoryDTO> getProductsByCategory() {
        List<Object[]> results = productRepository.countProductsByCategory();

        return results.stream()
                .map(result -> new ProductsByCategoryDTO(
                        (String) result[0],
                        ((Number) result[1]).intValue()
                ))
                .collect(Collectors.toList());
    }




}
