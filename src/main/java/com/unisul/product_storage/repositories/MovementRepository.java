package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Movement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(value = """
        SELECT p.name AS product_name, SUM(m.quantity) AS total_quantity
        FROM movements m
        JOIN products p ON p.id = m.product_id
        WHERE m.movement_type = 'ENTRY'
        GROUP BY p.name
        ORDER BY total_quantity DESC
        FETCH FIRST 1 ROWS ONLY
    """, nativeQuery = true)
    List<Object[]> findTopProductByEntrada();

    @Query(value = """
        SELECT p.name AS product_name, SUM(m.quantity) AS total_quantity
        FROM movements m
        JOIN products p ON p.id = m.product_id
        WHERE m.movement_type = 'EXIT'
        GROUP BY p.name
        ORDER BY total_quantity DESC
        FETCH FIRST 1 ROWS ONLY
    """, nativeQuery = true)
    List<Object[]> findTopProductBySaida();

    Page<Movement> findByProduct_NameContainingIgnoreCase(String name, Pageable pageable);


}
