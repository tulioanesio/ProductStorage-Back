package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.report.PriceListDTO;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mapper.ReportMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

}
