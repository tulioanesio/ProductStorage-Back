package com.unisul.product_storage.dtos.report.price_list;

import com.unisul.product_storage.dtos.category.CategorySimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Item da lista de preços, contendo informações do produto, valor e categoria.")
public record PriceListResponseDTO(

        @Schema(description = "Nome do produto.", example = "Notebook Lenovo Ideapad 3")
        @NotBlank(message = "O nome do produto não pode estar em branco")
        String name,

        @Schema(description = "Preço unitário do produto.", example = "3599.90")
        @NotNull(message = "O preço unitário não pode ser nulo")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço unitário deve ser maior que zero")
        BigDecimal unitPrice,

        @Schema(description = "Unidade de medida do produto.", example = "unidade")
        @NotBlank(message = "A unidade de medida não pode estar em branco")
        String unitOfMeasure,

        @Schema(description = "Categoria associada ao produto.")
        CategorySimpleDTO category
) {}