CREATE TABLE categories (
                            id SERIAL PRIMARY KEY,
                            nome VARCHAR(100) NOT NULL,
                            tamanho VARCHAR(20),
                            embalagem VARCHAR(20)
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
