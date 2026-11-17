package com.portfolio.inventory;

import java.math.BigDecimal;
import java.util.Objects;

/** Immutable product attributes; stock is maintained by InventoryService. */
public record Product(String sku, String name, BigDecimal unitPrice) {
    public Product {
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("SKU is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        Objects.requireNonNull(unitPrice, "Unit price is required");
        if (unitPrice.signum() < 0) throw new IllegalArgumentException("Unit price cannot be negative");
        sku = sku.trim();
        name = name.trim();
    }
}