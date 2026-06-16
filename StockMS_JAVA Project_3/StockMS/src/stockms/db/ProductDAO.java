package stockms.db;

import stockms.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean addProduct(Product p) {
        String sql = "INSERT INTO products (product_id, name, category, quantity, unit_price, " +
                     "supplier, mfg_date, exp_date, batch_no, low_stock_alert) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getCategory());
            ps.setInt   (4, p.getQuantity());
            ps.setDouble(5, p.getUnitPrice());
            ps.setString(6, p.getSupplier());
            ps.setString(7, p.getMfgDate());
            ps.setString(8, p.getExpDate());
            ps.setString(9, p.getBatchNo());
            ps.setInt   (10, p.getLowStockAlert());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("addProduct: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, unit_price=?, " +
                     "supplier=?, mfg_date=?, exp_date=?, batch_no=?, low_stock_alert=? WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setInt   (3, p.getQuantity());
            ps.setDouble(4, p.getUnitPrice());
            ps.setString(5, p.getSupplier());
            ps.setString(6, p.getMfgDate());
            ps.setString(7, p.getExpDate());
            ps.setString(8, p.getBatchNo());
            ps.setInt   (9, p.getLowStockAlert());
            ps.setString(10, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateProduct: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(String productId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE product_id=?")) {
            ps.setString(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteProduct: " + e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY name")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getAllProducts: " + e.getMessage());
        }
        return list;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR product_id LIKE ? OR category LIKE ? ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k); ps.setString(3, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("searchProducts: " + e.getMessage());
        }
        return list;
    }

    public List<Product> getLowStockProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity <= low_stock_alert ORDER BY quantity ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("getLowStock: " + e.getMessage());
        }
        return list;
    }

    public Product getProductById(String id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE product_id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("getProductById: " + e.getMessage());
        }
        return null;
    }

    public boolean productIdExists(String id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE product_id=?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { /* ignore */ }
        return false;
    }

    public int getTotalProducts() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    public double getTotalInventoryValue() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(quantity * unit_price) FROM products")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { /* ignore */ }
        return 0;
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
            rs.getString("product_id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getInt("quantity"),
            rs.getDouble("unit_price"),
            rs.getString("supplier"),
            rs.getString("mfg_date"),
            rs.getString("exp_date"),
            rs.getString("batch_no"),
            rs.getInt("low_stock_alert")
        );
    }
}
