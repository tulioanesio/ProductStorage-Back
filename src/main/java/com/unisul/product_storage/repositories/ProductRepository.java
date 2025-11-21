package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.stockAvailable < p.minStockQuantity")
    Page<Product> findLowStockProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockAvailable > p.maxStockQuantity")
    Page<Product> findHighStockProducts(Pageable pageable);

    @Query(value = """
           SELECT c.name AS categoryName, COUNT(DISTINCT p.id) AS productCount
           FROM Product p
           JOIN p.category c
           GROUP BY c.name
           """,
            countQuery = """
           SELECT COUNT(DISTINCT c.name)
           FROM Product p
           JOIN p.category c
           """)
    Page<Object[]> countProductsByCategory(Pageable pageable);

    @Query("""
           SELECT c.name AS categoryName, COUNT(DISTINCT p.id) AS productCount
           FROM Product p
           JOIN p.category c
           WHERE c.id = :categoryId
           GROUP BY c.name
           """)
    List<Object[]> countProductsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COALESCE(SUM(p.unitPrice * p.stockAvailable), 0) FROM Product p")
    BigDecimal calculateTotalStockValue();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchByName(@Param("name") String name, Pageable pageable);
}