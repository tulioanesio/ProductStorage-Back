package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import com.unisul.product_storage.dtos.report.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.LowStockProductsDTO;
import com.unisul.product_storage.dtos.report.PriceListDTO;
import com.unisul.product_storage.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReportMapper {

    public PriceListDTO toPriceListDTO(Product product) {
        return new PriceListDTO(
                product.getName(),
                product.getUnitPrice(),
                product.getUnitOfMeasure(),
                new CategorySimpleDTO(
                        product.getCategory().getId(),
                        product.getCategory().getName()
                )
        );
    }

    public InventoryBalanceDTO toInventoryBalanceDTO(Product product) {
        BigDecimal totalValue = product.getUnitPrice().multiply(BigDecimal.valueOf(product.getStockAvailable()));

        return new InventoryBalanceDTO(
                product.getName(),
                product.getStockAvailable(),
                totalValue
        );
    }

    public LowStockProductsDTO toLowStockProductsDTO(Product product) {
        return new LowStockProductsDTO(
                product.getName(),
                product.getStockAvailable(),
                product.getMinStockQuantity()
        );
    }
}

