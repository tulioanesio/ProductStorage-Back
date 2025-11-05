package com.unisul.product_storage.controllers.category;

import com.unisul.product_storage.dtos.category.CategoryRequestDTO;
import com.unisul.product_storage.dtos.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias de produtos")
public interface SwaggerCategoryController {

    @Operation(summary = "Criar uma category", description = "Cria uma nova category de produto no sistema")
    ResponseEntity<CategoryResponseDTO> createCategory(
            @Parameter(description = "Dados da category a ser criada", required = true)
            CategoryRequestDTO request
    );

    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista paginada de categorias")
    ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(
            @Parameter(description = "Informações de paginação e ordenação")
            Pageable pageable
    );

    @Operation(summary = "Buscar category por ID", description = "Retorna uma category pelo seu ID")
    ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID da category a ser buscada", example = "1", required = true)
            Long id
    );

    @Operation(summary = "Atualizar category", description = "Atualiza os dados de uma category pelo seu ID")
    ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID da category a ser atualizada", example = "1", required = true)
            Long id,
            @Parameter(description = "Dados atualizados da category", required = true)
            CategoryRequestDTO request
    );

    @Operation(summary = "Deletar category", description = "Remove uma category do sistema pelo seu ID")
    ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID da category a ser deletada", example = "1", required = true)
            Long id
    );
}
