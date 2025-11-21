package com.unisul.product_storage.controllers.report;

import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.most_movement_product.MostProductMovementResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.dtos.report.products_by_category.ProductsByCategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios de produtos, movimentações e estoque.")
public interface SwaggerReportController {

    @Operation(
            summary = "Lista de preços dos produtos",
            description = "Retorna a lista de produtos paginada contendo o nome, unidade e preço atual de cada item.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de preços retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PriceListResponseDTO.class)))
            }
    )
    @GetMapping("/price-list")
    ResponseEntity<Page<PriceListResponseDTO>> getPriceList(Pageable pageable);

    @Operation(
            summary = "Saldo de estoque",
            description = "Retorna o valor total do estoque e a lista de produtos com suas respectivas quantidades.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório de saldo de estoque gerado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryBalanceResponseDTO.class)))
            }
    )
    @GetMapping("/inventory-balance")
    ResponseEntity<InventoryBalanceResponseDTO> getInventoryBalance(Pageable pageable);

    @Operation(
            summary = "Produtos com baixo estoque",
            description = "Retorna os produtos cujo estoque atual está abaixo da quantidade mínima configurada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório de baixo estoque retornado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LowStockProductsResponseDTO.class))))
            }
    )
    @GetMapping("/low-stock-products")
    ResponseEntity<Page<LowStockProductsResponseDTO>> getLowStockProducts(Pageable pageable);

    @Operation(
            summary = "Produtos agrupados por categoria",
            description = "Retorna o número de produtos por categoria. Caso seja informado o ID da categoria, retorna apenas os produtos dessa categoria.",
            parameters = {
                    @Parameter(name = "categoryId", description = "ID da categoria (opcional)", example = "3")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório por categoria retornado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ProductsByCategoryResponseDTO.class))))
            }
    )
    @GetMapping("/products-by-category")
    ResponseEntity<Page<ProductsByCategoryResponseDTO>> getProductsByCategory(
            Pageable pageable,
            @RequestParam(required = false) Long categoryId
    );

    @Operation(
            summary = "Produtos agrupados por categoria",
            description = "Retorna o número de produtos por categoria. Caso seja informado o ID da categoria, retorna apenas os produtos dessa categoria.",
            parameters = {
                    @Parameter(name = "categoryId", description = "ID da categoria (opcional)", example = "3")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório por categoria retornado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/most-output-product")
    ResponseEntity<List<MostProductMovementResponseDTO>> getMostOutputProduct();

    @Operation(
            summary = "Produto com maior entrada",
            description = "Retorna uma lista (normalmente com 1 item) do produto que mais teve movimentações de entrada (compra, reposição, etc.). " +
                    "Caso não exista, retorna um array vazio [].",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista com o produto de maior entrada retornada com sucesso (ou vazia se nenhum encontrado)",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = MostProductMovementResponseDTO.class))
                            )
                    )
            }
    )
    @GetMapping("/most-input-product")
    ResponseEntity<List<MostProductMovementResponseDTO>> getMostInputProduct();
}