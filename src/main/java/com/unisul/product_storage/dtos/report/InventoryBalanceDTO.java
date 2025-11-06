package com.unisul.product_storage.dtos.report;

import java.math.BigDecimal;

public record InventoryBalanceDTO(
        String name,
        int stockAvailable,
        BigDecimal totalValue
) {}
