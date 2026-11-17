package com.portfolio.inventory;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    private Product product(String sku, String name, String price) {
        return new Product(sku, name, new BigDecimal(price));
    }

    @Test void addsAndFindsProducts() {
        InventoryService service = new InventoryService();
        service.addProduct(product("A1", "Adapter", "12.50"), 5);
        assertEquals(5, service.findBySku("A1").quantity());
        assertEquals(1, service.productCount());
        assertThrows(IllegalArgumentException.class,
                () -> service.addProduct(product("A1", "Another", "1.00"), 1));
    }

    @Test void restockAndSellWithSafetyChecks() {
        InventoryService service = new InventoryService();
        service.addProduct(product("A1", "Adapter", "12.50"), 5);
        service.restock("A1", 3);
        service.sell("A1", 2);
        assertEquals(6, service.findBySku("A1").quantity());
        assertThrows(IllegalArgumentException.class, () -> service.sell("A1", 7));
        assertThrows(IllegalArgumentException.class, () -> service.restock("A1", 0));
        assertEquals(6, service.findBySku("A1").quantity());
    }

    @Test void sortsAndReportsLowStock() {
        InventoryService service = new InventoryService();
        service.addProduct(product("Z", "Zebra", "2.00"), 9);
        service.addProduct(product("A", "Apple", "3.00"), 1);
        assertEquals("Apple", service.allByName().getFirst().product().name());
        assertEquals("A", service.lowStock(2).getFirst().product().sku());
    }

    @Test void binarySearchFindsSkuAndRejectsUnknown() {
        InventoryService service = new InventoryService();
        service.addProduct(product("B2", "Battery", "5.00"), 1);
        service.addProduct(product("A1", "Adapter", "10.00"), 2);
        assertEquals("Adapter", service.findByBinarySearch("A1").product().name());
        assertThrows(NoSuchElementException.class, () -> service.findByBinarySearch("NOPE"));
    }

    @Test void calculatesTotalValueWithoutFloatingPointError() {
        InventoryService service = new InventoryService();
        service.addProduct(product("A", "Apple", "0.10"), 3);
        service.addProduct(product("B", "Banana", "0.20"), 2);
        assertEquals(0, new BigDecimal("0.70").compareTo(service.totalInventoryValue()));
    }

    @Test void rejectsInvalidProductsAndQuantities() {
        InventoryService service = new InventoryService();
        assertThrows(IllegalArgumentException.class, () -> new Product("  ", "Item", BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class, () -> new Product("SKU", "Item", new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> service.addProduct(product("A", "Adapter", "2"), -1));
        assertThrows(NoSuchElementException.class, () -> service.findBySku("missing"));
    }
}