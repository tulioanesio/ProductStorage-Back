CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    size VARCHAR(20),
    packaging VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE products
    ADD COLUMN category_id INT;

ALTER TABLE products
    ADD CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories(id)
            ON DELETE SET NULL;

ALTER TABLE products
DROP COLUMN IF EXISTS categoria;
