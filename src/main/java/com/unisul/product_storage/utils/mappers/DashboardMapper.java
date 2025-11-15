package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.dashboard.DashboardResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DashboardMapper {

    public DashboardResponseDTO toDashboardDTO(
            int totalProducts,
            int lowStockProducts,
            int highStockProducts,
            BigDecimal totalStockValue
    ) {
        return new DashboardResponseDTO(
                totalProducts,
                lowStockProducts,
                highStockProducts,
                totalStockValue
        );
    }

}