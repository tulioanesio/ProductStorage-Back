package com.unisul.product_storage.dtos.report;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public record InventoryBalanceResponseDTO(
        BigDecimal stockValue,
        Page<InventoryBalanceDTO> items
) {}