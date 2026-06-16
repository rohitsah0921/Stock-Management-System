package stockms.db;

import stockms.model.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    /**
     * Records a purchase AND adds quantity to inventory.
     */
    public boolean recordPurchase(Purchase purchase) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert purchase record
            PreparedStatement ins = conn.prepareStatement(
                "INSERT INTO purchases (product_id, product_name, quantity, unit_cost, total_cost, supplier) VALUES (?,?,?,?,?,?)");
            ins.setString(1, purchase.getProductId());
            ins.setString(2, purchase.getProductName());
            ins.setInt   (3, purchase.getQuantity());
            ins.setDouble(4, purchase.getUnitCost());
            ins.setDouble(5, purchase.getTotalCost());
            ins.setString(6, purchase.getSupplier());
            ins.executeUpdate();

            // Add to inventory
            PreparedStatement upd = conn.prepareStatement(
                "UPDATE products SET quantity = quantity + ? WHERE product_id=?");
            upd.setInt   (1, purchase.getQuantity());
            upd.setString(2, purchase.getProductId());
            upd.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("recordPurchase: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { /* ignore */ }
        }
    }

    public List<Purchase> getAllPurchases() {
        List<Purchase> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM purchases ORDER BY purchase_date DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getAllPurchases: " + e.getMessage());
        }
        return list;
    }

    public double getTotalPurchaseCost() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COALESCE(SUM(total_cost),0) FROM purchases")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    private Purchase mapRow(ResultSet rs) throws SQLException {
        Purchase p = new Purchase();
        p.setPurchaseId  (rs.getInt("purchase_id"));
        p.setProductId   (rs.getString("product_id"));
        p.setProductName (rs.getString("product_name"));
        p.setQuantity    (rs.getInt("quantity"));
        p.setUnitCost    (rs.getDouble("unit_cost"));
        p.setTotalCost   (rs.getDouble("total_cost"));
        p.setSupplier    (rs.getString("supplier"));
        p.setPurchaseDate(rs.getString("purchase_date"));
        return p;
    }
}
