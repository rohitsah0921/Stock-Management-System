package stockms.ui;

import stockms.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final String username;
    private final String role;
    private final JPanel contentArea;

    // Sidebar nav buttons
    private JButton activeBtn = null;

    public MainFrame(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Stock Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        // ── Layout: sidebar + content ─────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIHelper.BG_DARK);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UIHelper.BG_DARK);
        root.add(contentArea, BorderLayout.CENTER);

        // Start on Dashboard
        showPanel(new DashboardPanel());
    }

    // ── Sidebar ───────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIHelper.BG_CARD);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIHelper.BORDER));

        // App name
        JLabel appName = new JLabel("  StockMS");
        appName.setForeground(UIHelper.ACCENT);
        appName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appName.setBorder(BorderFactory.createEmptyBorder(20, 10, 4, 10));
        appName.setAlignmentX(LEFT_ALIGNMENT);

        JLabel version = new JLabel("  v1.0  |  " + role.toUpperCase());
        version.setForeground(UIHelper.TEXT_MUTED);
        version.setFont(UIHelper.FONT_SMALL);
        version.setBorder(BorderFactory.createEmptyBorder(0, 10, 16, 10));
        version.setAlignmentX(LEFT_ALIGNMENT);

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(UIHelper.BORDER);
        sep1.setMaximumSize(new Dimension(210, 1));

        sidebar.add(appName);
        sidebar.add(version);
        sidebar.add(sep1);
        sidebar.add(Box.createVerticalStrut(8));

        // Nav items
        String[][] navItems = {
            {"[~]", "Dashboard"},
            {"[P]", "Products"},
            {"[$]", "New Sale"},
            {"[+]", "Purchase"},
            {"[S]", "Sales History"},
            {"[B]", "Purchase History"},
            {"[!]", "Low Stock"},
        };

        for (String[] item : navItems) {
            JButton btn = navButton(item[0] + "  " + item[1]);
            sidebar.add(btn);

            btn.addActionListener(e -> {
                setActiveNav(btn);
                switch (item[1]) {
                    case "Dashboard":        showPanel(new DashboardPanel());       break;
                    case "Products":         showPanel(new ProductPanel());         break;
                    case "New Sale":         showPanel(new SalePanel());            break;
                    case "Purchase":         showPanel(new PurchasePanel());        break;
                    case "Sales History":    showPanel(new SalesHistoryPanel());    break;
                    case "Purchase History": showPanel(new PurchaseHistoryPanel()); break;
                    case "Low Stock":        showPanel(new LowStockPanel());        break;
                }
            });

            // Auto-select Dashboard
            if (item[1].equals("Dashboard")) {
                setActiveNav(btn);
            }
        }

        sidebar.add(Box.createVerticalGlue());

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(UIHelper.BORDER);
        sep2.setMaximumSize(new Dimension(210, 1));
        sidebar.add(sep2);

        // User info
        JLabel userLbl = new JLabel("  [U]  " + username);
        userLbl.setForeground(UIHelper.TEXT_PRIMARY);
        userLbl.setFont(UIHelper.FONT_BODY);
        userLbl.setBorder(BorderFactory.createEmptyBorder(12, 10, 4, 10));
        userLbl.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(userLbl);

        JButton logoutBtn = navButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(12));

        return sidebar;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(UIHelper.BG_CARD);
        btn.setForeground(UIHelper.TEXT_MUTED);
        btn.setFont(UIHelper.FONT_BODY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setMaximumSize(new Dimension(210, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private void setActiveNav(JButton btn) {
        if (activeBtn != null) {
            activeBtn.setBackground(UIHelper.BG_CARD);
            activeBtn.setForeground(UIHelper.TEXT_MUTED);
        }
        btn.setBackground(new Color(56, 189, 248, 30));
        btn.setForeground(UIHelper.ACCENT);
        activeBtn = btn;
    }

    public void showPanel(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
}
