package stockms.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Automatically creates all required tables if they don't exist.
 * Run once on application startup.
 */
public class DBInit {

    public static void initialize() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Products / Inventory table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS products (" +
                "  product_id   VARCHAR(20)  NOT NULL PRIMARY KEY," +
                "  name         VARCHAR(100) NOT NULL," +
                "  category     VARCHAR(50)  NOT NULL," +
                "  quantity     INT          NOT NULL DEFAULT 0," +
                "  unit_price   DOUBLE       NOT NULL DEFAULT 0.0," +
                "  supplier     VARCHAR(100)," +
                "  mfg_date     VARCHAR(20)," +
                "  exp_date     VARCHAR(20)," +
                "  batch_no     VARCHAR(30)," +
                "  low_stock_alert INT       NOT NULL DEFAULT 10," +
                "  created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            // Sales table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sales (" +
                "  sale_id      INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  product_id   VARCHAR(20)  NOT NULL," +
                "  product_name VARCHAR(100) NOT NULL," +
                "  quantity     INT          NOT NULL," +
                "  unit_price   DOUBLE       NOT NULL," +
                "  total_price  DOUBLE       NOT NULL," +
                "  customer     VARCHAR(100)," +
                "  sale_date    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            // Purchase / Restock table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS purchases (" +
                "  purchase_id  INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  product_id   VARCHAR(20)  NOT NULL," +
                "  product_name VARCHAR(100) NOT NULL," +
                "  quantity     INT          NOT NULL," +
                "  unit_cost    DOUBLE       NOT NULL," +
                "  total_cost   DOUBLE       NOT NULL," +
                "  supplier     VARCHAR(100)," +
                "  purchase_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            // Users table (for login)
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  user_id      INT          NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "  username     VARCHAR(50)  NOT NULL UNIQUE," +
                "  password     VARCHAR(100) NOT NULL," +
                "  role         VARCHAR(20)  NOT NULL DEFAULT 'staff'," +
                "  created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
            );

            // Insert default admin if not exists
            stmt.executeUpdate(
                "INSERT IGNORE INTO users (username, password, role) " +
                "VALUES ('admin', 'admin123', 'admin')"
            );

            // Insert sample products if table is empty
            stmt.executeUpdate(
                "INSERT IGNORE INTO products (product_id, name, category, quantity, unit_price, supplier, mfg_date, exp_date, batch_no, low_stock_alert) VALUES" +
                "('P001','Paracetamol 500mg','Medicine',200,5.50,'MedSupply Co','01/01/2024','01/01/2026','BT-001',20)," +
                "('P002','Vitamin C Tablets','Supplement',150,12.00,'HealthPlus','01/03/2024','01/03/2026','BT-002',15)," +
                "('P003','Basmati Rice 5kg','Grocery',80,320.00,'FoodCorp','10/02/2024','10/02/2025','BT-003',10)," +
                "('P004','Sunflower Oil 1L','Grocery',60,180.00,'OilMills','05/01/2024','05/01/2025','BT-004',10)," +
                "('P005','Hand Sanitizer','Hygiene',8,75.00,'CleanCo','01/06/2024','01/06/2025','BT-005',10)"
            );

            System.out.println("[DBInit] All tables ready.");

        } catch (SQLException e) {
            System.err.println("[DBInit] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
