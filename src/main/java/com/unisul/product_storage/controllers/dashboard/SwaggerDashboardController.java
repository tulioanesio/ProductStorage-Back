package com.unisul.product_storage.controllers.dashboard;

import com.unisul.product_storage.dtos.dashboard.DashboardResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Dashboard",
        description = "Endpoint para visualizar dados consolidados do sistema"
)
public interface SwaggerDashboardController {

    @Operation(
            summary = "Obter dados do dashboard",
            description = "Retorna informações consolidadas como totais, métricas e estatísticas do sistema"
    )
    ResponseEntity<DashboardResponseDTO> getDashboard(
            @Parameter(description = "Parâmetros de paginação (caso aplicável ao dashboard)")
            Pageable pageable
    );
}