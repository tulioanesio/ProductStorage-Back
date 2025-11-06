package com.unisul.product_storage.dtos.report;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceListDTO(

        @NotBlank(message = "O nome do produto não pode estar em branco")
        String name,

        @NotNull(message = "O preço unitário não pode ser nulo")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço unitário deve ser maior que zero")
        BigDecimal unitPrice,

        @NotBlank(message = "A unidade de medida não pode estar em branco")
        String unitOfMeasure,

        @NotNull(message = "A categoria não pode ser nula")
        CategorySimpleDTO category
) {
}
