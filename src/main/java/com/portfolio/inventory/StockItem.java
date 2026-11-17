package com.portfolio.inventory;

/** Snapshot view: quantity is never negative. */
public record StockItem(Product product, int quantity) {
    public StockItem {
        if (product == null) throw new IllegalArgumentException("Product is required");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
    }
}