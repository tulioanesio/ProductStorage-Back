package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.dashboard.DashboardResponseDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mappers.DashboardMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final ProductRepository productRepository;

    public DashboardService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public DashboardResponseDTO getDashboardData(Pageable pageable) {

        long totalProducts = productRepository.findAll(pageable).getTotalElements();

        long lowStockCount = productRepository.findLowStockProducts(Pageable.unpaged())
                .getTotalElements();

        long highStockCount = productRepository.findHighStockProducts(Pageable.unpaged())
                .getTotalElements();

        BigDecimal totalStockValue = productRepository.calculateTotalStockValue();

        return new DashboardResponseDTO(
                totalProducts,
                lowStockCount,
                highStockCount,
                totalStockValue
        );
    }
}