package stockms.ui;

import stockms.db.DBInit;
import stockms.db.UserDAO;
import stockms.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField     userField;
    private final JPasswordField passField;
    private final UserDAO        userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Stock Management System — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Initialize DB ─────────────────────────────────────────────
        try {
            DBInit.initialize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database connection failed!\n\n" + e.getMessage() +
                "\n\nMake sure MySQL is running and update DBConnection.java",
                "DB Error", JOptionPane.ERROR_MESSAGE);
        }

        // ── Root panel ────────────────────────────────────────────────
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UIHelper.BG_DARK);
        setContentPane(root);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIHelper.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        card.setPreferredSize(new Dimension(340, 400));

        // Logo / icon area
        JLabel icon = new JLabel("", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("StockMS", SwingConstants.CENTER);
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Stock Management System", SwingConstants.CENTER);
        subtitle.setFont(UIHelper.FONT_SMALL);
        subtitle.setForeground(UIHelper.TEXT_MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        // Form
        userField = UIHelper.styledField();
        passField = UIHelper.styledPasswordField();

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 8));
        formPanel.setBackground(UIHelper.BG_CARD);
        formPanel.setMaximumSize(new Dimension(280, 120));
        formPanel.add(UIHelper.label("Username"));
        formPanel.add(userField);
        formPanel.add(UIHelper.label("Password"));
        formPanel.add(passField);

        JButton loginBtn = UIHelper.primaryButton("Login");
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(280, 38));
        loginBtn.addActionListener(e -> doLogin());

        JLabel hint = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        hint.setFont(UIHelper.FONT_SMALL);
        hint.setForeground(UIHelper.TEXT_MUTED);
        hint.setAlignmentX(CENTER_ALIGNMENT);

        // Enter key support
        passField.addActionListener(e -> doLogin());

        card.add(icon);
        card.add(Box.createVerticalStrut(6));
        card.add(title);
        card.add(Box.createVerticalStrut(2));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(formPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(hint);

        root.add(card);
    }

    private void doLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = userDAO.authenticate(username, password);
        if (role != null) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(username, role).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
        }
    }
}
