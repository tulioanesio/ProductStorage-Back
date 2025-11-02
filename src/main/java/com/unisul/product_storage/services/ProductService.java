package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.ProductRequestDTO;
import com.unisul.product_storage.dtos.ProductResponseDTO;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO data) {
        Product product = new Product();
        product.setName(data.name());
        product.setUnitPrice(data.unitPrice());
        product.setUnit(data.unit());
        product.setStockQuantity(data.stockQuantity());
        product.setMinStockQuantity(data.minStockQuantity());
        product.setMaxStockQuantity(data.maxStockQuantity());
        product.setCategory(data.category());

        Product saved = productRepository.save(product);
        return toResponseDTO(saved);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO data) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setName(data.name());
        product.setUnitPrice(data.unitPrice());
        product.setUnit(data.unit());
        product.setStockQuantity(data.stockQuantity());
        product.setMinStockQuantity(data.minStockQuantity());
        product.setMaxStockQuantity(data.maxStockQuantity());
        product.setCategory(data.category());

        Product updated = productRepository.save(product);
        return toResponseDTO(updated);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        productRepository.deleteById(id);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getUnit(),
                product.getStockQuantity(),
                product.getMinStockQuantity(),
                product.getMaxStockQuantity(),
                product.getCategory()
        );
    }
}
