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

    @Operation(summary = "Criar uma categoria", description = "Cria uma nova categoria de produto no sistema")
    ResponseEntity<CategoryResponseDTO> createCategory(
            @Parameter(description = "Dados da categoria a ser criada", required = true)
            CategoryRequestDTO request
    );

    @Operation(
            summary = "Listar todas as categorias",
            description = "Retorna uma lista paginada de categorias, com opção de filtrar por nome"
    )
    ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(
            @Parameter(description = "Filtro opcional pelo nome da categoria", example = "Eletrônicos")
            String name,

            @Parameter(description = "Informações de paginação e ordenação")
            Pageable pageable
    );

    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria pelo seu ID")
    ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID da categoria", example = "1", required = true)
            Long id
    );

    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria pelo seu ID")
    ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID da categoria a ser atualizada", example = "1", required = true)
            Long id,
            @Parameter(description = "Dados atualizados da categoria", required = true)
            CategoryRequestDTO request
    );

    @Operation(summary = "Deletar categoria", description = "Remove uma categoria do sistema pelo seu ID")
    ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID da categoria a ser deletada", example = "1", required = true)
            Long id
    );
}
