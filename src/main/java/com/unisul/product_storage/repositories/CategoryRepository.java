package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
