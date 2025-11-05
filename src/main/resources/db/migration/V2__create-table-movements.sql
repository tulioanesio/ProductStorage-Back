CREATE TABLE movements (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL,
    movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL,
    movement_type VARCHAR(10) NOT NULL CHECK (movement_type IN ('ENTRY', 'EXIT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
            REFERENCES products(id)
            ON DELETE CASCADE
);
