package com.unisul.product_storage.controllers;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.services.MovementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final MovementService movementService;

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
        return ResponseEntity.ok(movementService.getMovementById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovementResponseDTO> update(@Valid @PathVariable("id") Long id, @RequestBody MovementRequestDTO data) {
        return ResponseEntity.ok(movementService.updateMovement(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovementResponseDTO> deleteById(@PathVariable("id") Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }

}
