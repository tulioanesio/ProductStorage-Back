CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    unit_of_measure VARCHAR(20) NOT NULL,
    stock_available INT NOT NULL DEFAULT 0,
    min_quantity INT NOT NULL DEFAULT 0,
    max_quantity INT,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
