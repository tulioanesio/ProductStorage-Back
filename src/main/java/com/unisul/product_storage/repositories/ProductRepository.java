package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
}
