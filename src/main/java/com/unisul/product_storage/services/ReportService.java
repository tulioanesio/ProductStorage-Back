package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.report.InventoryBalanceDTO;
import com.unisul.product_storage.dtos.report.InventoryBalanceResponseDTO;
import com.unisul.product_storage.dtos.report.PriceListDTO;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mapper.ReportMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportService {

    private final ProductRepository productRepository;
    private final ReportMapper reportMapper;

    public ReportService(ProductRepository productRepository, ReportMapper reportMapper) {
        this.productRepository = productRepository;
        this.reportMapper = reportMapper;
    }

    public Page<PriceListDTO> getPriceList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(reportMapper::toPriceListDTO);
    }

    public InventoryBalanceResponseDTO getInventoryBalance(Pageable pageable) {
        Page<InventoryBalanceDTO> inventoryPage = productRepository.findAll(pageable)
                .map(reportMapper::toInventoryBalanceDTO);

        BigDecimal stockValue = productRepository.findAll().stream()
                .map(product -> product.getUnitPrice()
                        .multiply(BigDecimal.valueOf(product.getStockAvailable())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventoryBalanceResponseDTO(stockValue, inventoryPage);
    }


}
