package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Category;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.CategoryRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO data) {
        if (data.name() == null || data.name().isBlank()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Nome do produto inválido",
                    "O nome do produto não pode ser nulo ou vazio."
            );
        }

        Category category = null;
        if (data.categoryId() != null) {
            category = categoryRepository.findById(data.categoryId())
                    .orElseThrow(() -> new BusinessException(
                            HttpStatus.NOT_FOUND,
                            "Categoria não encontrada",
                            "Categoria com ID " + data.categoryId() + " não existe."
                    ));
        }

        Product product = productMapper.toEntity(data, category);
        Product saved = productRepository.save(product);
        return productMapper.toResponseDTO(saved);
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toResponseDTO);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado",
                        "Produto com ID " + id + " não foi encontrado."
                ));
        return productMapper.toResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO data) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado",
                        "Não foi possível atualizar o produto. ID " + id + " inexistente."
                ));

        Category category = null;
        if (data.categoryId() != null) {
            category = categoryRepository.findById(data.categoryId())
                    .orElseThrow(() -> new BusinessException(
                            HttpStatus.NOT_FOUND,
                            "Categoria não encontrada",
                            "Categoria com ID " + data.categoryId() + " não existe."
                    ));
        }

        productMapper.updateEntity(product, data, category);
        Product updated = productRepository.save(product);
        return productMapper.toResponseDTO(updated);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado",
                    "Não foi possível excluir o produto. ID " + id + " não existe."
            );
        }
        productRepository.deleteById(id);
    }
}