# Inventory Management System — Java 21

A Java portfolio project demonstrating object-oriented programming (OOP), data structures and algorithms (DSA), unit testing, and CI with GitHub Actions.

**Status:** Phase 1 — console application. Products are stored in memory, and inventory resets whenever the program exits. A web interface, authentication, persistent database, and deployment are planned but not implemented.

## Features

- Register products with SKU, name, unit price, and starting quantity.
- Look up products by SKU and prevent duplicates.
- Increase stock (restock) and decrease stock (sell), with validation.
- Sort products by name and list products at or below a low-stock threshold.
- Demonstrate binary search on SKUs.
- Calculate inventory value accurately with `BigDecimal`.
- Six JUnit 5 automated tests and GitHub Actions CI.

## Requirements

- JDK 21
- Apache Maven 3.9+
- Git (for cloning)

Verify installations:

```bash
java -version
mvn -version
git --version
```

## Clone and run

```bash
git clone https://github.com/gairepawan/inventory-management-system.git
cd inventory-management-system
mvn clean verify
java -cp target/classes com.portfolio.inventory.App
```

Expected output:

```text
INVENTORY (sorted by product name)
KEY-003 | Keyboard | qty=12 | $50.00
LAP-001 | Laptop | qty=6 | $750.00
MOU-002 | Mouse | qty=4 | $25.00
LOW STOCK (at most 5):
MOU-002
TOTAL VALUE: $5200.00
```

To run tests only:

```bash
mvn test
```

## Project structure

```text
inventory-management-system/
├── .github/workflows/ci.yml
├── .gitignore
├── pom.xml
├── README.md
└── src/
    ├── main/java/com/portfolio/inventory/
    │   ├── App.java
    │   ├── Product.java
    │   ├── StockItem.java
    │   └── InventoryService.java
    └── test/java/com/portfolio/inventory/
        └── InventoryServiceTest.java
```

**App:** CLI demo. **Product:** immutable product attributes. **StockItem:** product + quantity snapshot. **InventoryService:** business logic, storage, searching, sorting, and totals.

## DSA and time complexity

| Operation | Algorithm / structure | Time complexity |
| --- | --- | --- |
| Product lookup by SKU | `HashMap.get()` | O(1) average |
| Add product | `HashMap.put()` | O(1) average |
| Restock / sell | Hash map lookup and replacement | O(1) average |
| List alphabetically | Copy to `ArrayList` and sort with `Comparator` | O(n log n) |
| Identify low stock | Sort + filter | O(n log n) |
| Binary SKU search | Sort + binary search | O(n log n) overall, O(log n) after sorting |
| Total inventory value | Iterate over all products | O(n) |

Hash map operations have average-case rather than guaranteed worst-case O(1) bounds. The binary-search method sorts a fresh list on every call as a learning exercise; production systems should typically use the hash map for direct lookup.

## Java usage example

```java
InventoryService inventory = new InventoryService();
inventory.addProduct(new Product("SKU-100", "USB Cable", new BigDecimal("12.50")), 10);
inventory.restock("SKU-100", 5);
inventory.sell("SKU-100", 2);
System.out.println(inventory.findBySku("SKU-100").quantity()); // 13
System.out.println(inventory.totalInventoryValue());
```

Import `com.portfolio.inventory.*` and `java.math.BigDecimal` when using this snippet in another package.

## Testing and CI/CD

JUnit 5 covers product registration, duplicate SKU validation, stock adjustments, insufficient-stock rejection, sorting, low-stock reporting, binary SKU search, exact decimal totals, and invalid inputs.

The GitHub Actions workflow at `.github/workflows/ci.yml` runs for pushes to `main` and pull requests targeting `main`. It installs Java 21 and runs:

```bash
mvn --batch-mode verify
```

See **GitHub → Actions** for build results. This is **Continuous Integration**, not deployment yet.

## Git workflow for new features

```bash
git switch -c feature/stock-history
# Update source and tests
mvn clean verify
git add .
git commit -m "feat: add stock history"
git push -u origin feature/stock-history
```

Then open a pull request, review checks, and merge into `main`.

## Roadmap

1. Console menu and richer product filters.
2. Stock movement history and audit trail.
3. Spring Boot REST API and PostgreSQL persistence.
4. User authentication and role-based access.
5. Browser dashboard, integration tests, monitoring, and deployment.

## Troubleshooting

- `mvn` not found: install Maven and restart the terminal.
- Java version mismatch: `mvn -version` must report Java 21.
- Main class missing: run `mvn clean verify` before launching.
- CI failure: open the failed run under **Actions** and review Maven logs.

## Security and license

Do not commit credentials, sensitive records, or environment secrets. The current program is for demonstrations and learning, not production inventory management. No open-source license has been chosen; default copyright rules apply.
