package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.stockAvailable < p.minStockQuantity")
    Page<Product> findLowStockProducts(Pageable pageable);
}
