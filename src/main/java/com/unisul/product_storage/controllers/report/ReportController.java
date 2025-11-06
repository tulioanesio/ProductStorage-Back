package com.unisul.product_storage.controllers.report;

import com.unisul.product_storage.dtos.report.*;
import com.unisul.product_storage.services.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/price-list")
    public ResponseEntity<Page<PriceListDTO>> getPriceList(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<PriceListDTO> priceList = reportService.getPriceList(pageable);
        return ResponseEntity.ok(priceList);
    }

    @GetMapping("/inventory-balance")
    public ResponseEntity<InventoryBalanceResponseDTO> getInventoryBalance(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        InventoryBalanceResponseDTO response = reportService.getInventoryBalance(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<Page<LowStockProductsDTO>> getLowStockProducts(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<LowStockProductsDTO> response = reportService.getLowStockProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products-by-category")
    public ResponseEntity<List<ProductsByCategoryDTO>> getProductsByCategory() {
        List<ProductsByCategoryDTO> report = reportService.getProductsByCategory();
        return ResponseEntity.ok(report);
    }
}
