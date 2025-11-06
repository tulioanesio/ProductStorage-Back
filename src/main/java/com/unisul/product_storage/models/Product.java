package com.unisul.product_storage.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "unit_of_measure", nullable = false)
    private String unitOfMeasure;

    @Column(name = "stock_available", nullable = false)
    private int stockAvailable;

    @Column(name = "min_quantity", nullable = false)
    private int minStockQuantity;

    @Column(name = "max_quantity")
    private int maxStockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(Long id, String name, BigDecimal unitPrice, String unitOfMeasure,
                   int stockAvailable, int minQuantity, int maxQuantity, Category category) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unitOfMeasure = unitOfMeasure;
        this.stockAvailable = stockAvailable;
        this.minStockQuantity = minQuantity;
        this.maxStockQuantity = maxQuantity;
        this.category = category;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public int getStockAvailable() { return stockAvailable; }
    public void setStockAvailable(int stockAvailable) { this.stockAvailable = stockAvailable; }

    public int getMinStockQuantity() { return minStockQuantity; }
    public void setMinStockQuantity(int minStockQuantity) { this.minStockQuantity = minStockQuantity; }

    public int getMaxStockQuantity() { return maxStockQuantity; }
    public void setMaxStockQuantity(int maxQuantity) { this.maxStockQuantity = maxQuantity; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}