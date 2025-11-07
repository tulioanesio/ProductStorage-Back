package com.unisul.product_storage.controllers.movement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.dtos.product.ProductSummaryDTO;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.services.MovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovementService movementService;

    @Autowired
    private ObjectMapper objectMapper;

    private MovementRequestDTO requestDTO;
    private MovementResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new MovementRequestDTO(
                1L,
                LocalDate.now(),
                10,
                MovementType.ENTRY
        );

        ProductSummaryDTO productSummary = new ProductSummaryDTO(
                1L,
                "Produto Teste",
                new BigDecimal("99.90"),
                "unidade"
        );

        responseDTO = new MovementResponseDTO(
                1L,
                productSummary,
                LocalDate.now(),
                10,
                MovementType.ENTRY,
                "Normal"
        );
    }

    @Test
    void createMovement_ShouldReturnCreatedMovement() throws Exception {
        Mockito.when(movementService.createMovement(any(MovementRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product.name").value("Produto Teste"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.movementType").value("ENTRY"))
                .andExpect(jsonPath("$.status").value("Normal"));
    }

    @Test
    void getAllMovements_ShouldReturnPagedResult() throws Exception {
        Mockito.when(movementService.getAllMovements(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(responseDTO)));

        mockMvc.perform(get("/movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].product.name").value("Produto Teste"))
                .andExpect(jsonPath("$.content[0].product.unitPrice").value(99.90))
                .andExpect(jsonPath("$.content[0].product.unitOfMeasure").value("unidade"));
    }

    @Test
    void getMovementById_ShouldReturnMovement() throws Exception {
        Mockito.when(movementService.getMovementById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/movements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product.name").value("Produto Teste"));
    }

    @Test
    void updateMovement_ShouldReturnUpdatedMovement() throws Exception {
        Mockito.when(movementService.updateMovement(eq(1L), any(MovementRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/movements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.movementType").value("ENTRY"));
    }

    @Test
    void deleteMovement_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/movements/1"))
                .andExpect(status().isNoContent());
    }
}