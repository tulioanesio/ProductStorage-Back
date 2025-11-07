package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MovementService service;

    private Product product;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setStockAvailable(10);
        product.setMaxStockQuantity(50);
        product.setMinStockQuantity(2);
    }

    @Nested
    @DisplayName("Testes de Criação (Create)")
    class CreateMovementTests {

        @Test
        @DisplayName("Deve lançar BusinessException ao criar se produto não for encontrado")
        void createMovement_ShouldThrow_WhenProductNotFound() {
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);
            when(productRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.createMovement(dto));
        }

        @Test
        @DisplayName("Deve ajustar estoque e status 'Normal' para entrada")
        void createMovement_ShouldAdjustStock_ForEntry() {
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.createMovement(dto);

            assertEquals(15, product.getStockAvailable());
            assertEquals("Normal", result.status());
            assertEquals(5, result.quantity());
        }

        @Test
        @DisplayName("Deve definir status 'Estoque ultrapassou o limite' se entrada exceder o máximo")
        void createMovement_ShouldSetStatus_WhenStockExceedsMax() {
            product.setStockAvailable(48);
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.createMovement(dto);

            assertEquals(53, product.getStockAvailable());
            assertEquals("Estoque ultrapassou o limite máximo permitido!", result.status());
        }

        @Test
        @DisplayName("Deve lançar BusinessException se estoque for insuficiente para saída")
        void createMovement_ShouldThrow_WhenStockInsufficientOnExit() {
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 15, MovementType.EXIT);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            BusinessException ex = assertThrows(BusinessException.class, () -> service.createMovement(dto));
            assertEquals("Estoque insuficiente", ex.getBusinessMessage());
        }

        @Test
        @DisplayName("Deve definir status 'Estoque caiu abaixo do limite' se saída for abaixo do mínimo")
        void createMovement_ShouldSetStatus_WhenStockBelowMinAfterExit() {
            product.setStockAvailable(5);
            product.setMinStockQuantity(2);
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 4, MovementType.EXIT);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.createMovement(dto);

            assertEquals(1, product.getStockAvailable());
            assertEquals("Estoque caiu abaixo do limite mínimo permitido!", result.status());
        }

        @Test
        @DisplayName("Deve ajustar estoque e status 'Normal' para saída válida")
        void createMovement_ShouldExitNormally_WhenStockAboveMin() {
            product.setStockAvailable(10);
            product.setMinStockQuantity(2);
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.EXIT);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.createMovement(dto);

            assertEquals(5, product.getStockAvailable());
            assertEquals("Normal", result.status());
        }
    }

    @Nested
    @DisplayName("Testes de Leitura (Read)")
    class ReadMovementTests {

        @Test
        @DisplayName("Deve retornar página de movimentações")
        void getAllMovements_ShouldReturnPage() {
            Movement movement = new Movement();
            movement.setId(1L);
            movement.setProduct(product);
            movement.setQuantity(10);
            movement.setMovementType(MovementType.ENTRY);

            Page<Movement> page = new PageImpl<>(List.of(movement), PageRequest.of(0, 10), 1);
            when(movementRepository.findAll(any(Pageable.class))).thenReturn(page);

            Page<MovementResponseDTO> result = service.getAllMovements(PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
            assertEquals(1L, result.getContent().get(0).product().id());
        }

        @Test
        @DisplayName("Deve lançar BusinessException ao buscar ID inexistente")
        void getMovementById_ShouldThrow_WhenNotFound() {
            when(movementRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.getMovementById(1L));
        }

        @Test
        @DisplayName("Deve retornar DTO quando movimentação é encontrada")
        void getMovementById_ShouldReturnDto_WhenFound() {
            Movement movement = new Movement();
            movement.setId(1L);
            movement.setProduct(product);
            movement.setQuantity(10);
            movement.setMovementType(MovementType.ENTRY);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

            MovementResponseDTO result = service.getMovementById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(10, result.quantity());
            assertEquals(1L, result.product().id());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateMovementTests {

        @Test
        @DisplayName("Deve lançar BusinessException ao atualizar se movimentação não for encontrada")
        void updateMovement_ShouldThrow_WhenMovementNotFound() {
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);
            when(movementRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.updateMovement(1L, dto));
        }

        @Test
        @DisplayName("Deve lançar BusinessException ao atualizar se produto não for encontrado")
        void updateMovement_ShouldThrow_WhenProductNotFound() {
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);
            when(movementRepository.findById(1L)).thenReturn(Optional.of(new Movement()));
            when(productRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(BusinessException.class, () -> service.updateMovement(1L, dto));
        }

        @Test
        @DisplayName("Deve desfazer estoque anterior (ENTRADA) e aplicar novo (ENTRADA)")
        void updateMovement_ShouldAdjustStock_WhenEntryToEntry() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.ENTRY);
            movement.setQuantity(5);
            movement.setProduct(product);
            product.setStockAvailable(15);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 3, MovementType.ENTRY);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            service.updateMovement(1L, dto);

            assertEquals(13, product.getStockAvailable());
        }

        @Test
        @DisplayName("Deve desfazer estoque anterior (SAÍDA) e aplicar novo (SAÍDA)")
        void updateMovement_ShouldAdjustStock_WhenExitToExit() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.EXIT);
            movement.setQuantity(3);
            movement.setProduct(product);
            product.setStockAvailable(7);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 2, MovementType.EXIT);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            service.updateMovement(1L, dto);

            assertEquals(8, product.getStockAvailable());
        }

        @Test
        @DisplayName("Deve ajustar estoque ao mudar tipo de ENTRADA para SAÍDA")
        void updateMovement_ShouldAdjustStock_WhenMovementTypeChangesEntryToExit() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.ENTRY);
            movement.setQuantity(5);
            movement.setProduct(product);
            product.setStockAvailable(15);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 3, MovementType.EXIT);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO updated = service.updateMovement(1L, dto);

            assertEquals(7, product.getStockAvailable());
            assertEquals(MovementType.EXIT, updated.movementType());
        }

        @Test
        @DisplayName("Deve ajustar estoque ao mudar tipo de SAÍDA para ENTRADA")
        void updateMovement_ShouldAdjustStock_WhenMovementTypeChangesExitToEntry() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.EXIT);
            movement.setQuantity(3);
            movement.setProduct(product);
            product.setStockAvailable(7);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO updated = service.updateMovement(1L, dto);

            assertEquals(15, product.getStockAvailable());
            assertEquals(MovementType.ENTRY, updated.movementType());
        }

        @Test
        @DisplayName("Deve lançar BusinessException se estoque for insuficiente na atualização")
        void updateMovement_ShouldThrow_WhenStockInsufficientOnExit() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.EXIT);
            movement.setQuantity(3);
            movement.setProduct(product);
            product.setStockAvailable(7);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 15, MovementType.EXIT);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            BusinessException ex = assertThrows(BusinessException.class, () -> service.updateMovement(1L, dto));
            assertEquals("Estoque insuficiente", ex.getBusinessMessage());
        }

        @Test
        @DisplayName("Deve definir status 'Estoque ultrapassou o limite' na atualização")
        void updateMovement_ShouldSetStatus_WhenStockExceedsMaxOnUpdate() {
            product.setStockAvailable(48);
            product.setMaxStockQuantity(50);

            Movement movement = new Movement();
            movement.setMovementType(MovementType.ENTRY);
            movement.setQuantity(2);
            movement.setProduct(product);
            product.setStockAvailable(50);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, MovementType.ENTRY);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO updated = service.updateMovement(1L, dto);

            assertEquals(53, product.getStockAvailable());
            assertEquals("Estoque ultrapassou o limite máximo permitido!", updated.status());
        }

        @Test
        @DisplayName("Deve definir status 'Estoque caiu abaixo do limite' na atualização")
        void updateMovement_ShouldSetStatus_WhenStockBelowMinAfterExit() {
            product.setStockAvailable(10);
            product.setMinStockQuantity(5);

            Movement existingMovement = new Movement();
            existingMovement.setMovementType(MovementType.ENTRY);
            existingMovement.setQuantity(2);
            existingMovement.setProduct(product);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 8, MovementType.EXIT);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(existingMovement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.updateMovement(1L, dto);

            assertEquals(0, product.getStockAvailable());
            assertEquals("Estoque caiu abaixo do limite mínimo permitido!", result.status());
        }
    }

    @Nested
    @DisplayName("Testes de Exclusão (Delete)")
    class DeleteMovementTests {

        @Test
        @DisplayName("Deve lançar BusinessException ao excluir ID inexistente")
        void deleteMovement_ShouldThrow_WhenNotExists() {
            when(movementRepository.existsById(1L)).thenReturn(false);
            assertThrows(BusinessException.class, () -> service.deleteMovement(1L));
        }

        @Test
        @DisplayName("Deve chamar deleteById quando ID existir")
        void deleteMovement_ShouldDelete_WhenExists() {
            when(movementRepository.existsById(1L)).thenReturn(true);
            doNothing().when(movementRepository).deleteById(1L);

            assertDoesNotThrow(() -> service.deleteMovement(1L));
            verify(movementRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Testes de Cobertura de Branch (Paths Nulos)")
    class BranchCoverageTests {

        @Test
        @DisplayName("Deve cobrir branch 'else' em ajustarEstoqueProduto com tipo nulo")
        void createMovement_ShouldCoverElseBranch_WhenMovementTypeIsNull() {
            product.setStockAvailable(10);
            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 5, null);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            MovementResponseDTO result = service.createMovement(dto);

            assertEquals(10, product.getStockAvailable());
            assertEquals(5, result.quantity());
            assertEquals("Normal", result.status());
        }

        @Test
        @DisplayName("Deve cobrir branch 'else' em desfazerEstoqueAnterior com tipo nulo")
        void updateMovement_ShouldCoverElseBranch_WhenOriginalMovementTypeIsNull() {
            product.setStockAvailable(10);

            Movement movement = new Movement();
            movement.setMovementType(null);
            movement.setQuantity(5);
            movement.setProduct(product);

            MovementRequestDTO dto = new MovementRequestDTO(1L, today, 3, MovementType.ENTRY);

            when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(movementRepository.save(any(Movement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            service.updateMovement(1L, dto);

            assertEquals(13, product.getStockAvailable());
        }
    }
}