# 📦 Stock Management System
> A full Java Swing desktop application with MySQL backend

---

## Features

| Module | What it does |
|---|---|
| 🔐 Login | Secure login with username/password |
| 🏠 Dashboard | Live stats: total products, inventory value, revenue, low-stock alerts |
| 📦 Products | Full CRUD — Add, View, Edit, Delete, Search products |
| 🛒 New Sale | Select product, enter quantity → auto-deducts stock, records transaction |
| 📥 Purchase | Record restocking → auto-adds to inventory |
| 📊 Sales History | View all past sales with totals |
| 📋 Purchase History | View all past purchases |
| ⚠️ Low Stock | Alerts for products below threshold (color-coded) |

---

## Project Structure

```
StockMS/
├── src/
│   └── stockms/
│       ├── Main.java                  ← Entry point (run this)
│       ├── db/
│       │   ├── DBConnection.java      ← ❗ Update credentials here
│       │   ├── DBInit.java            ← Auto-creates tables on startup
│       │   ├── ProductDAO.java        ← Product DB operations
│       │   ├── SaleDAO.java           ← Sales DB operations
│       │   ├── PurchaseDAO.java       ← Purchase DB operations
│       │   └── UserDAO.java           ← Login authentication
│       ├── model/
│       │   ├── Product.java
│       │   ├── Sale.java
│       │   └── Purchase.java
│       ├── ui/
│       │   ├── LoginFrame.java
│       │   ├── MainFrame.java
│       │   ├── DashboardPanel.java
│       │   ├── ProductPanel.java
│       │   ├── SalePanel.java
│       │   ├── PurchasePanel.java
│       │   ├── SalesHistoryPanel.java
│       │   ├── PurchaseHistoryPanel.java
│       │   └── LowStockPanel.java
│       └── utils/
│           └── UIHelper.java          ← All colors, fonts, styled components
├── setup_database.sql                 ← Run once in MySQL
└── README.md
```

---

## Quick Start

### Step 1 — Requirements
- Java JDK 8 or higher
- NetBeans IDE (or IntelliJ IDEA / VS Code)
- XAMPP (for MySQL) or standalone MySQL

### Step 2 — Create the Database
Run in phpMyAdmin or MySQL CLI:
```sql
CREATE DATABASE stockms;
```
That's it. All tables are created automatically on first launch.

### Step 3 — Add MySQL JDBC Driver
Download `mysql-connector-java` JAR and add to project classpath.
- NetBeans: Right-click Project → Properties → Libraries → Add JAR
- IntelliJ: File → Project Structure → Libraries → Add

### Step 4 — Update DB Credentials
Open `src/stockms/db/DBConnection.java` and update:
```java
private static final String DB_URL  = "jdbc:mysql://localhost:3306/stockms?...";
private static final String USER    = "root";       // your username
private static final String PASSWORD = "";           // your password
```

### Step 5 — Run
Run `src/stockms/Main.java`

Login with: **admin / admin123**

---

## Database Tables (auto-created)

| Table | Purpose |
|---|---|
| `users` | Login credentials |
| `products` | Inventory / product catalog |
| `sales` | All recorded sales transactions |
| `purchases` | All recorded purchase/restocking entries |

---

## Default Login
| Username | Password | Role |
|---|---|---|
| admin | admin123 | admin |

---

## Technologies
- **Java** (JDK 8+) — Application logic
- **Java Swing** — GUI framework
- **MySQL / MariaDB** — Database
- **JDBC** — Database connectivity
- **NetBeans / IntelliJ** — IDE (any works)
