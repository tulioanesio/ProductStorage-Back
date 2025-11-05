package com.unisul.product_storage.services;

import com.unisul.product_storage.dtos.movement.MovementRequestDTO;
import com.unisul.product_storage.dtos.movement.MovementResponseDTO;
import com.unisul.product_storage.exceptions.handler.BusinessException;
import com.unisul.product_storage.models.Movement;
import com.unisul.product_storage.models.MovementType;
import com.unisul.product_storage.models.Product;
import com.unisul.product_storage.repositories.MovementRepository;
import com.unisul.product_storage.repositories.ProductRepository;
import com.unisul.product_storage.utils.mapper.MovementMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        Product product = productRepository.findById(data.productId())
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado",
                        "Produto informado não existe no banco de dados."
                ));

        Movement movement = MovementMapper.toEntity(data, product);
        ajustarEstoqueProduto(movement, product);

        Movement saved = movementRepository.save(movement);
        return MovementMapper.toResponseDTO(saved);
    }

    public Page<MovementResponseDTO> getAllMovements(Pageable pageable) {
        Page<Movement> movements = movementRepository.findAll(pageable);
        return movements.map(MovementMapper::toResponseDTO);
    }

    public MovementResponseDTO getMovementById(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Movimentação não encontrada",
                        "Movimentação com ID " + id + " não foi encontrada."
                ));
        return MovementMapper.toResponseDTO(movement);
    }

    public MovementResponseDTO updateMovement(Long id, MovementRequestDTO data) {
        Movement existing = movementRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Movimentação não encontrada",
                        "Não foi possível atualizar. Movimento com ID " + id + " inexistente."
                ));

        Product product = productRepository.findById(data.productId())
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado",
                        "Produto informado não existe no banco de dados."
                ));

        desfazerEstoqueAnterior(existing, product);

        existing.setMovementDate(data.movementDate());
        existing.setMovementType(data.movementType());
        existing.setQuantity(data.quantity());
        existing.setProduct(product);

        ajustarEstoqueProduto(existing, product);

        productRepository.save(product);
        Movement updated = movementRepository.save(existing);

        return MovementMapper.toResponseDTO(updated);
    }

    public void deleteMovement(Long id) {
        if (!movementRepository.existsById(id)) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "Movimento não encontrado",
                    "Não foi possível excluir. Movimento com ID " + id + " não existe."
            );
        }
        movementRepository.deleteById(id);
    }

    private void ajustarEstoqueProduto(Movement movement, Product product) {
        int currentStock = product.getStockAvailable();
        int qty = movement.getQuantity();

        if (movement.getMovementType() == MovementType.ENTRY) {
            product.setStockAvailable(currentStock + qty);
            if (product.getStockAvailable() > product.getMaxQuantity()) {
                movement.setStatus("Estoque ultrapassou o limite máximo permitido!");
            }
        } else if (movement.getMovementType() == MovementType.EXIT) {
            if (currentStock < qty) {
                throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente",
                        "Não há estoque suficiente para realizar esta saída."
                );
            }
            product.setStockAvailable(currentStock - qty);
            if (product.getStockAvailable() < product.getMinQuantity()) {
                movement.setStatus("Estoque caiu abaixo do limite mínimo permitido!");
            }
        }
    }

    private void desfazerEstoqueAnterior(Movement movement, Product product) {
        int qty = movement.getQuantity();
        if (movement.getMovementType() == MovementType.ENTRY) {
            product.setStockAvailable(product.getStockAvailable() - qty);
        } else if (movement.getMovementType() == MovementType.EXIT) {
            product.setStockAvailable(product.getStockAvailable() + qty);
        }
    }
}
