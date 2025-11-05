package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import com.unisul.product_storage.dtos.report.PriceListDTO;
import com.unisul.product_storage.models.Product;
import org.springframework.stereotype.Component;

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
}

