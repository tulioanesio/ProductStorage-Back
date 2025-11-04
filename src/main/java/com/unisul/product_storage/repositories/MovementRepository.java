package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}
