package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReportMapper {

    public PriceListResponseDTO toPriceListDTO(Product product) {
        return new PriceListResponseDTO(
                product.getName(),
                product.getUnitPrice(),
                product.getUnitOfMeasure(),
                new CategorySimpleDTO(
                        product.getCategory() != null ? product.getCategory().getId() : null,
                        product.getCategory() != null ? product.getCategory().getName() : "Sem categoria"
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

    public LowStockProductsResponseDTO toLowStockProductsDTO(Product product) {
        return new LowStockProductsResponseDTO(
                product.getName(),
                product.getStockAvailable(),
                product.getMinStockQuantity()
        );
    }
}

