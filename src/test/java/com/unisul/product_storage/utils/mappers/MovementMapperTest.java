package com.unisul.product_storage.utils.mappers;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.dtos.product.ProductSummaryDTO;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MovementMapperTest {

    private Product product;
    private MovementRequestDTO movementRequestDTO;
    private Movement movement;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setName("Notebook");
        product.setUnitPrice(BigDecimal.valueOf(3000.00));
        product.setUnitOfMeasure("unidade");

        movementRequestDTO = new MovementRequestDTO(
                1L,
                LocalDate.now(),
                10,
                MovementType.ENTRY
        );

        movement = new Movement();
        movement.setId(1L);
        movement.setProduct(product);
        movement.setMovementDate(LocalDate.now());
        movement.setQuantity(10);
        movement.setMovementType(MovementType.ENTRY);
        movement.setStatus("Normal");
    }

    @Nested
    @DisplayName("Testes de Mapeamento")
    class MappingTests {

        @Test
        @DisplayName("Deve mapear DTO de Request para Entidade")
        void toEntity_ShouldMapDtoToEntity() {
            Movement result = MovementMapper.toEntity(movementRequestDTO, product);

            assertNotNull(result);
            assertEquals(product, result.getProduct());
            assertEquals(10, result.getQuantity());
            assertEquals(MovementType.ENTRY, result.getMovementType());
            assertEquals(movementRequestDTO.movementDate(), result.getMovementDate());
        }

        @Test
        @DisplayName("Deve mapear Entidade para DTO de Response")
        void toResponseDTO_ShouldMapEntityToDto() {
            MovementResponseDTO result = MovementMapper.toResponseDTO(movement);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(10, result.quantity());
            assertEquals(MovementType.ENTRY, result.movementType());
            assertEquals("Normal", result.status());

            ProductSummaryDTO productSummary = result.product();
            assertNotNull(productSummary);
            assertEquals(1L, productSummary.id());
            assertEquals("Notebook", productSummary.name());
            assertEquals(BigDecimal.valueOf(3000.00), productSummary.unitPrice());
            assertEquals("unidade", productSummary.unitOfMeasure());

        }
    }

    @Nested
    @DisplayName("Testes de Cobertura")
    class CoverageTests {

        @Test
        @DisplayName("Deve cobrir o construtor privado")
        void testPrivateConstructor() throws Exception {
            Constructor<MovementMapper> constructor = MovementMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            assertDoesNotThrow(() -> constructor.newInstance(), "A instanciação via reflexão não deve falhar.");
            assertNotNull(constructor.newInstance());
        }
    }
}