package com.portfolio.inventory;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addProduct(new Product("LAP-001", "Laptop", new BigDecimal("750.00")), 8);
        inventory.addProduct(new Product("MOU-002", "Mouse", new BigDecimal("25.00")), 3);
        inventory.addProduct(new Product("KEY-003", "Keyboard", new BigDecimal("50.00")), 12);
        inventory.sell("LAP-001", 2);
        inventory.restock("MOU-002", 1);

        System.out.println("INVENTORY (sorted by product name)");
        inventory.allByName().forEach(item -> System.out.printf("%s | %s | qty=%d | $%s%n",
                item.product().sku(), item.product().name(), item.quantity(), item.product().unitPrice()));
        System.out.println("LOW STOCK (at most 5):");
        inventory.lowStock(5).forEach(item -> System.out.println(item.product().sku()));
        System.out.println("TOTAL VALUE: $" + inventory.totalInventoryValue().toPlainString());
    }
}