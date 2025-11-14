package com.unisul.product_storage.controllers.dashboard;

import com.unisul.product_storage.dtos.dashboard.DashboardResponseDTO;
import com.unisul.product_storage.services.DashboardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        DashboardResponseDTO result = dashboardService.getDashboardData(pageable);
        return ResponseEntity.ok(result);
    }

}