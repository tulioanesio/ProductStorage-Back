package com.unisul.product_storage.controllers;

import com.unisul.product_storage.dtos.MovementRequestDTO;
import com.unisul.product_storage.dtos.MovementResponseDTO;
import com.unisul.product_storage.dtos.ProductRequestDTO;
import com.unisul.product_storage.dtos.ProductResponseDTO;
import com.unisul.product_storage.services.MovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping
    public ResponseEntity<MovementResponseDTO> create(@Valid @RequestBody MovementRequestDTO data) {
        MovementResponseDTO created = movementService.createMovement(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<MovementResponseDTO>> getAll(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movementService.getAllMovements(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementResponseDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movementService.getProductById(id));
    }

}
