package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.PriceListDTO;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ProductRepository productRepository;

    public ReportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<PriceListDTO> getPriceList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toPriceListDTO);
    }

    private PriceListDTO toPriceListDTO(Product product) {
        return new PriceListDTO(
                product.getName(),
                product.getUnitPrice(),
                product.getUnit(),
                product.getCategory()
        );
    }
}
