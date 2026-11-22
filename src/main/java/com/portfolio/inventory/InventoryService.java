package com.portfolio.inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** In-memory inventory service for learning collections, sorting and binary search. */
public class InventoryService {
    private final Map<String, StockItem> items = new HashMap<>();

    public void addProduct(Product product, int initialQuantity) {
        if (product == null) throw new IllegalArgumentException("Product is required");
        if (initialQuantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        if (items.containsKey(product.sku())) throw new IllegalArgumentException("Duplicate SKU: " + product.sku());
        items.put(product.sku(), new StockItem(product, initialQuantity));
    }

    public StockItem findBySku(String sku) {
        StockItem item = items.get(sku);
        if (item == null) throw new NoSuchElementException("Unknown SKU: " + sku);
        return item;
    }

    public void restock(String sku, int amount) {
        positiveAmount(amount);
        StockItem old = findBySku(sku);
        items.put(sku, new StockItem(old.product(), Math.addExact(old.quantity(), amount)));
    }

    public void sell(String sku, int amount) {
        positiveAmount(amount);
        StockItem old = findBySku(sku);
        if (amount > old.quantity()) throw new IllegalArgumentException("Insufficient stock for " + sku);
        items.put(sku, new StockItem(old.product(), old.quantity() - amount));
    }

    public List<StockItem> allByName() {
        List<StockItem> result = new ArrayList<>(items.values());
        result.sort(Comparator.comparing((StockItem x) -> x.product().name(), String.CASE_INSENSITIVE_ORDER)
                .thenComparing(x -> x.product().sku()));
        return result;
    }

    public List<StockItem> lowStock(int threshold) {
        if (threshold < 0) throw new IllegalArgumentException("Threshold cannot be negative");
        return allByName().stream().filter(item -> item.quantity() <= threshold).toList();
    }

    /** Binary search on sorted SKU list; sorting dominates this call's complexity. */
    public StockItem findByBinarySearch(String sku) {
        if (sku == null) throw new IllegalArgumentException("SKU is required");
        List<StockItem> sorted = new ArrayList<>(items.values());
        sorted.sort(Comparator.comparing(x -> x.product().sku()));
        int lo = 0, hi = sorted.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            StockItem candidate = sorted.get(mid);
            int comparison = candidate.product().sku().compareTo(sku);
            if (comparison == 0) return candidate;
            if (comparison < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        throw new NoSuchElementException("Unknown SKU: " + sku);
    }

    public BigDecimal totalInventoryValue() {
        return items.values().stream().map(item -> item.product().unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int productCount() { return items.size(); }

    private static void positiveAmount(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
    }

    /** Case-insensitive substring search, with results sorted by product name. */
    public List<StockItem> searchByName(String query) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("Search query is required");
        String needle = query.strip().toLowerCase(java.util.Locale.ROOT);
        return allByName().stream()
                .filter(item -> item.product().name().toLowerCase(java.util.Locale.ROOT).contains(needle))
                .toList();
    }
}