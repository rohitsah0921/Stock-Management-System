package stockms.db;

import java.sql.*;

public class UserDAO {

    /**
     * Returns the user's role ("admin"/"staff") on success, null on failure.
     */
    public String authenticate(String username, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (SQLException e) {
            System.err.println("authenticate: " + e.getMessage());
        }
        return null;
    }

    public boolean changePassword(String username, String oldPass, String newPass) {
        String role = authenticate(username, oldPass);
        if (role == null) return false;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET password=? WHERE username=?")) {
            ps.setString(1, newPass);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("changePassword: " + e.getMessage());
            return false;
        }
    }
}
