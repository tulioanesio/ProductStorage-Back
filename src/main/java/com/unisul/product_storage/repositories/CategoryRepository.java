package com.unisul.product_storage.repositories;

import com.unisul.product_storage.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
