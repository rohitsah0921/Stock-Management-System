package stockms.db;

import stockms.model.Sale;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    /**
     * Records a sale AND deducts quantity from inventory.
     * Returns false if stock is insufficient.
     */
    public boolean recordSale(Sale sale) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Check stock
            PreparedStatement chk = conn.prepareStatement("SELECT quantity FROM products WHERE product_id=?");
            chk.setString(1, sale.getProductId());
            ResultSet rs = chk.executeQuery();
            if (!rs.next() || rs.getInt(1) < sale.getQuantity()) {
                conn.rollback();
                return false;
            }

            // Insert sale record
            PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO sales (product_id, product_name, quantity, unit_price, total_price, customer) VALUES (?,?,?,?,?,?)");
            ins.setString(1, sale.getProductId());
            ins.setString(2, sale.getProductName());
            ins.setInt   (3, sale.getQuantity());
            ins.setDouble(4, sale.getUnitPrice());
            ins.setDouble(5, sale.getTotalPrice());
            ins.setString(6, sale.getCustomer());
            ins.executeUpdate();

            // Deduct inventory
            PreparedStatement upd = conn.prepareStatement(
                "UPDATE products SET quantity = quantity - ? WHERE product_id=?");
            upd.setInt   (1, sale.getQuantity());
            upd.setString(2, sale.getProductId());
            upd.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("recordSale: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
    }

    public List<Sale> getAllSales() {
        List<Sale> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sales ORDER BY sale_date DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getAllSales: " + e.getMessage());
        }
        return list;
    }

    public double getTodaySalesTotal() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT COALESCE(SUM(total_price),0) FROM sales WHERE DATE(sale_date) = CURDATE()")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    public double getTotalSalesRevenue() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COALESCE(SUM(total_price),0) FROM sales")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    public int getTotalSalesCount() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM sales")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    private Sale mapRow(ResultSet rs) throws SQLException {
        Sale s = new Sale();
        s.setSaleId     (rs.getInt("sale_id"));
        s.setProductId  (rs.getString("product_id"));
        s.setProductName(rs.getString("product_name"));
        s.setQuantity   (rs.getInt("quantity"));
        s.setUnitPrice  (rs.getDouble("unit_price"));
        s.setTotalPrice (rs.getDouble("total_price"));
        s.setCustomer   (rs.getString("customer"));
        s.setSaleDate   (rs.getString("sale_date"));
        return s;
    }
}
