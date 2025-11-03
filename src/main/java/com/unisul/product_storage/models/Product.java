package com.unisul.product_storage.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private String unit;
    private int stockQuantity;
    private int minStockQuantity;
    private int maxStockQuantity;
    private String category;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal unitPrice, String unit, int stockQuantity, int minStockQuantity, int maxStockQuantity, String category) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.minStockQuantity = minStockQuantity;
        this.maxStockQuantity = maxStockQuantity;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getMinStockQuantity() {
        return minStockQuantity;
    }

    public void setMinStockQuantity(int minStockQuantity) {
        this.minStockQuantity = minStockQuantity;
    }

    public int getMaxStockQuantity() {
        return maxStockQuantity;
    }

    public void setMaxStockQuantity(int maxStockQuantity) {
        this.maxStockQuantity = maxStockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
