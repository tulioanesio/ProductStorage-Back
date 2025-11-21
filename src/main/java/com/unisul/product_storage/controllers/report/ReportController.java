package com.unisul.product_storage.controllers.report;

import com.unisul.product_storage.dtos.report.inventory_balance.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.low_stock_products.LowStockProductsResponseDTO;
import com.unisul.product_storage.dtos.report.most_movement_product.MostProductMovementResponseDTO;
import com.unisul.product_storage.dtos.report.price_list.PriceListResponseDTO;
import com.unisul.product_storage.dtos.report.products_by_category.ProductsByCategoryResponseDTO;
import com.unisul.product_storage.services.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController implements SwaggerReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/price-list")
    public ResponseEntity<Page<PriceListResponseDTO>> getPriceList(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getPriceList(pageable));
    }

    @GetMapping("/inventory-balance")
    public ResponseEntity<InventoryBalanceResponseDTO> getInventoryBalance(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getInventoryBalance(pageable));
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<Page<LowStockProductsResponseDTO>> getLowStockProducts(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(reportService.getLowStockProducts(pageable));
    }

    @GetMapping("/products-by-category")
    public ResponseEntity<Page<ProductsByCategoryResponseDTO>> getProductsByCategory(@PageableDefault(page = 0, size = 20, sort = "categoryName", direction = Sort.Direction.ASC)
                                                                                     Pageable pageable,
                                                                                     @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(reportService.getProductsByCategory(pageable, categoryId));
    }

    @GetMapping("/most-output-product")
    public ResponseEntity<List<MostProductMovementResponseDTO>> getMostOutputProduct() {
        return ResponseEntity.ok(reportService.getMostOutputProduct());
    }

    @GetMapping("/most-input-product")
    public ResponseEntity<List<MostProductMovementResponseDTO>> getMostInputProduct() {
        return ResponseEntity.ok(reportService.getMostInputProduct());
    }
}
