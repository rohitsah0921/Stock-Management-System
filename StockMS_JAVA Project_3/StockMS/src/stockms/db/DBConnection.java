package stockms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton database connection manager.
 * Update DB_URL, USER, and PASSWORD to match your MySQL setup.
 */
public class DBConnection {

    // ── (!) CHANGE THESE TO MATCH YOUR SETUP ──────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/stockms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER    = "root";
    private static final String PASSWORD = "";          // XAMPP default: empty
    // ─────────────────────────────────────────────────────────────────

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector JAR to classpath.\n" + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
