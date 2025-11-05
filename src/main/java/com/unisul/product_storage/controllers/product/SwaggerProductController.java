package com.unisul.product_storage.controllers.product;

import com.unisul.product_storage.dtos.product.ProductRequestDTO;
import com.unisul.product_storage.dtos.product.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public interface SwaggerProductController {

    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto no sistema")
    ResponseEntity<ProductResponseDTO> create(
            @Parameter(description = "Dados do produto a serem criados", required = true)
            ProductRequestDTO data
    );

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista paginada de produtos")
    ResponseEntity<Page<ProductResponseDTO>> getAll(
            @Parameter(description = "Informações de paginação e ordenação")
            Pageable pageable
    );

    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto pelo seu ID")
    ResponseEntity<ProductResponseDTO> getById(
            @Parameter(description = "ID do produto a ser buscado", example = "1", required = true)
            Long id
    );

    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto pelo seu ID")
    ResponseEntity<ProductResponseDTO> update(
            @Parameter(description = "ID do produto a ser atualizado", example = "1", required = true)
            Long id,
            @Parameter(description = "Dados atualizados do produto", required = true)
            ProductRequestDTO data
    );

    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema pelo seu ID")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID do produto a ser deletado", example = "1", required = true)
            Long id
    );
}