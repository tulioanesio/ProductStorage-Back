# Product Storage Backend

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Overview

The **Product Storage** system is a [full stack](https://github.com/tulioanesio/ProductStorage-Front.git) application designed to manage the inventory of a commercial business efficiently.  
It provides full CRUD operations for products, categories, stock movements and reports, helping businesses maintain accurate stock levels and plan purchases effectively.

---

## Features

### Product Management
- Create, read, update, and delete products.
- Classify products by category.
- Recalculate all product prices by a given percentage.

### Category Management
- Create, read, update, and delete categories.
- Define size (Small, Medium, Large) and packaging type (Can, Glass, Plastic).
- Associate products with specific categories for organized stock control.

### Stock Movements
- Register product **entries** and **exits**.
- Automatically update product stock after each movement.
- Notify when stock is below minimum or above maximum levels.

### Reporting
1. **Price List:**  
   Displays all products alphabetically with their unit price, unit of measure, and category.
2. **Physical/Financial Balance:**  
   Shows product quantities, total value per product, and the total stock value.
3. **Low Stock Report:**  
   Lists products below the minimum quantity with name, minimum quantity, and current stock.
4. **Products by Category:**  
   Displays categories with the number of distinct products in each.
5. **Most Moved Products:**  
   Shows which products had the most entries and exits.

---

## Data Model

### Product
- **name:** product name
- **unitPrice:** unit price
- **unit:** unit of measure
- **stockQuantity:** current stock quantity
- **minStockQuantity:** minimum stock threshold
- **maxStockQuantity:** maximum stock threshold
- **category:** associated category

### Category
- **name:** category name
- **size:** Small, Medium, or Large
- **package:** type of packaging (Can, Glass, Plastic)

### Movement
- **productName:** product involved in the movement
- **movementDate:** date of the movement
- **quantity:** quantity moved
- **movementType:** Entry or Exit

---

## Technologies

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Prometheus & Grafana** (monitoring)
- **Swagger (OpenAPI)** for API documentation

---

## Running the Application

### Requirements
- Docker and Docker Compose installed
- Java 17 or higher
- Maven

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/tulioanesio/ProductStorage-Back.git
   cd ProductStorage-Back

2. Start containers:
    ```bash
   docker-compose up -d

3. Access the application:
    ```bash
    API: http://localhost:8080
    Swagger UI: http://localhost:8080/swagger-ui.html
    Prometheus: http://localhost:9090
    Grafana: http://localhost:3456

## License

This project is licensed under the [MIT License](LICENSE)

## Developers

* João Pedro Farias Da Silva
* Kaike Augusto Dos Santos
* Pedro Henrique Nieto da Silva
* Thuysa Monique Luvison da Rosa
* Tulio Anesio da Rosa
* Vinicius Freitas da Silva
