package com.unisul.product_storage.utils.mapper;

import com.unisul.product_storage.dtos.categoria.CategoryResponseDTO;
import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto, Category category) {
        Product product = new Product();
        product.setName(dto.name());
        product.setUnitPrice(dto.unitPrice());
        product.setUnitOfMeasure(dto.unitOfMeasure());
        product.setStockAvailable(dto.availableStock());
        product.setMinQuantity(dto.minStockQuantity());
        product.setMaxQuantity(dto.maxStockQuantity());
        product.setCategory(category);
        return product;
    }

    public void updateEntity(Product product, ProductRequestDTO dto, Category category) {
        product.setName(dto.name());
        product.setUnitPrice(dto.unitPrice());
        product.setUnitOfMeasure(dto.unitOfMeasure());
        product.setStockAvailable(dto.availableStock());
        product.setMinQuantity(dto.minStockQuantity());
        product.setMaxQuantity(dto.maxStockQuantity());
        product.setCategory(category);
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        Category category = product.getCategory();
        CategoryResponseDTO categoryDTO = null;

        if (category != null) {
            categoryDTO = new CategoryResponseDTO(
                    category.getId(),
                    category.getName(),
                    category.getSize(),
                    category.getPackaging()
            );
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getUnitOfMeasure(),
                product.getStockAvailable(),
                product.getMinQuantity(),
                product.getMaxQuantity(),
                categoryDTO
        );
    }
}
