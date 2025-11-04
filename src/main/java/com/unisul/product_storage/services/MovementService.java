package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.MovementRequestDTO;
import com.unisul.product_storage.dtos.MovementResponseDTO;
import com.unisul.product_storage.dtos.ProductRequestDTO;
import com.unisul.product_storage.dtos.ProductResponseDTO;
import com.unisul.product_storage.exceptions.MovementNotFoundException;
import com.unisul.product_storage.exceptions.NotEnoughStockException;
import com.unisul.product_storage.exceptions.ProductNotFoundException;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

    private final MovementRepository movementRepository;

    private final ProductRepository productRepository;

    public MovementService(MovementRepository movementRepository, ProductRepository productRepository) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
    }

    public MovementResponseDTO createMovement(MovementRequestDTO data) {
        Movement movement = new Movement();
        movement.setProduct(data.product());
        movement.setMovementDate(data.movementDate());
        movement.setQuantity(data.quantity());
        movement.setMovementType(data.movementType());

        Product product = productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        int currentStock = product.getStockQuantity();
        int movementQty = movement.getQuantity();

        if (movement.getMovementType() == MovementType.ENTRY) {
            product.setStockQuantity(currentStock + movementQty);
            if (product.getStockQuantity() > product.getMaxStockQuantity()) {
                movement.setStatus("Product stock exceeded the maximum limit!");
            }
        } else if (movement.getMovementType() == MovementType.EXIT) {
            if (currentStock < movementQty) {
                throw new NotEnoughStockException("Not enough stock for this movement");
            }
            product.setStockQuantity(currentStock - movementQty);
            if (product.getStockQuantity() < product.getMinStockQuantity()) {
                movement.setStatus("Product stock fell below the minimum limit!");
            }
        }

        movement.setProduct(product);

        Movement saved = movementRepository.save(movement);
        return toResponseDTO(saved);
    }

    public Page<MovementResponseDTO> getAllMovements(Pageable pageable) {
        return movementRepository.findAll(pageable)
                .map(this::toResponseDTO);

    }

    public MovementResponseDTO getProductById(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new MovementNotFoundException("Movement with id " + id + " not found."));
        return toResponseDTO(movement);
    }

    public MovementResponseDTO updateMovement(Long id, MovementRequestDTO data) {
        Movement existing = movementRepository.findById(id)
                .orElseThrow(() -> new MovementNotFoundException("Movement with id " + id + " not found."));

        Product product = productRepository.findById(data.product().getId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));

        int oldQty = existing.getQuantity();
        MovementType oldType = existing.getMovementType();

        if (oldType == MovementType.ENTRY) {
            product.setStockQuantity(product.getStockQuantity() - oldQty);
        } else if (oldType == MovementType.EXIT) {
            product.setStockQuantity(product.getStockQuantity() + oldQty);
        }

        int newQty = data.quantity();
        MovementType newType = data.movementType();

        if (newType == MovementType.ENTRY) {
            product.setStockQuantity(product.getStockQuantity() + newQty);
            if (product.getStockQuantity() > product.getMaxStockQuantity()) {
                existing.setStatus("Product stock exceeded the maximum limit!");
            }
        } else if (newType == MovementType.EXIT) {
            if (product.getStockQuantity() < newQty) {
                throw new NotEnoughStockException("Not enough stock for this movement");
            }
            product.setStockQuantity(product.getStockQuantity() - newQty);
            if (product.getStockQuantity() < product.getMinStockQuantity()) {
                existing.setStatus("Product stock fell below the minimum limit!");
            }
        }

        existing.setProduct(product);
        existing.setQuantity(newQty);
        existing.setMovementDate(data.movementDate());
        existing.setMovementType(newType);

        productRepository.save(product);
        Movement updated = movementRepository.save(existing);

        return toResponseDTO(updated);
    }


    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new MovementNotFoundException("Movement with id " + id + " not found.");
        }
        movementRepository.deleteById(id);
    }

    private MovementResponseDTO toResponseDTO(Movement movement) {
        return new MovementResponseDTO(
                movement.getId(),
                movement.getProduct(),
                movement.getMovementDate(),
                movement.getQuantity(),
                movement.getMovementType(),
                movement.getStatus()
        );
    }
}
