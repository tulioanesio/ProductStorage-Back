package com.unisul.product_storage.controllers.movement;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Movimentações", description = "Endpoints para gerenciamento de movimentações de produtos")
public interface SwaggerMovementController {

    @Operation(
            summary = "Criar uma movimentação",
            description = "Registra uma nova movimentação de produto no sistema"
    )
    ResponseEntity<MovementResponseDTO> create(
            @Parameter(description = "Dados da movimentação a ser criada", required = true)
            MovementRequestDTO data
    );

    @Operation(
            summary = "Listar todas as movimentações",
            description = """
                Retorna uma lista paginada de movimentações.
                É possível filtrar pelo nome do produto movimentado usando o query param 'name'.
                """
    )
    ResponseEntity<Page<MovementResponseDTO>> getAll(
            @Parameter(description = "Informações de paginação e ordenação")
            Pageable pageable,

            @Parameter(description = "Nome do produto para filtrar as movimentações", example = "Notebook")
            @RequestParam(required = false) String name
    );

    @Operation(
            summary = "Buscar movimentação por ID",
            description = "Retorna uma movimentação pelo seu ID"
    )
    ResponseEntity<MovementResponseDTO> getById(
            @Parameter(description = "ID da movimentação a ser buscada", example = "1", required = true)
            Long id
    );

    @Operation(
            summary = "Atualizar movimentação",
            description = "Atualiza os dados de uma movimentação existente"
    )
    ResponseEntity<MovementResponseDTO> update(
            @Parameter(description = "ID da movimentação a ser atualizada", example = "1", required = true)
            Long id,

            @Parameter(description = "Novos dados da movimentação", required = true)
            MovementRequestDTO data
    );

    @Operation(
            summary = "Deletar movimentação",
            description = "Remove uma movimentação do sistema pelo seu ID"
    )
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID da movimentação a ser deletada", example = "1", required = true)
            Long id
    );
}
