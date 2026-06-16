package stockms.ui;

import stockms.db.*;
import stockms.model.Product;
import stockms.utils.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final ProductDAO  productDAO  = new ProductDAO();
    private final SaleDAO     saleDAO     = new SaleDAO();
    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(UIHelper.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Title ─────────────────────────────────────────────────────
        JLabel title = UIHelper.heading("Dashboard");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIHelper.BG_DARK);

        // ── Stat Cards ────────────────────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        statsRow.setBackground(UIHelper.BG_DARK);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        int    totalProducts  = productDAO.getTotalProducts();
        double inventoryValue = productDAO.getTotalInventoryValue();
        double totalRevenue   = saleDAO.getTotalSalesRevenue();
        int    lowStockCount  = productDAO.getLowStockProducts().size();

        statsRow.add(UIHelper.statCard(String.valueOf(totalProducts),   "Total Products",    UIHelper.ACCENT));
        statsRow.add(UIHelper.statCard("₹" + String.format("%.0f", inventoryValue), "Inventory Value", UIHelper.ACCENT_GREEN));
        statsRow.add(UIHelper.statCard("₹" + String.format("%.0f", totalRevenue),   "Total Revenue",   UIHelper.ACCENT_AMBER));
        statsRow.add(UIHelper.statCard(String.valueOf(lowStockCount),   "Low Stock Items",   lowStockCount > 0 ? UIHelper.ACCENT_RED : UIHelper.ACCENT_GREEN));

        body.add(statsRow);
        body.add(Box.createVerticalStrut(24));

        // ── Low Stock Alert Table ─────────────────────────────────────
        JLabel lowTitle = UIHelper.heading("(!)  Low Stock Alerts");
        lowTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        body.add(lowTitle);

        String[] cols = {"Product ID", "Name", "Category", "In Stock", "Alert Level"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Product> lowStock = productDAO.getLowStockProducts();
        if (lowStock.isEmpty()) {
            model.addRow(new Object[]{"—", "All products are well-stocked ✓", "—", "—", "—"});
        } else {
            for (Product p : lowStock) {
                model.addRow(new Object[]{
                    p.getProductId(), p.getName(), p.getCategory(),
                    p.getQuantity(), p.getLowStockAlert()
                });
            }
        }

        JTable table = new JTable(model);
        UIHelper.styleTable(table);

        // Color low stock rows red
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(CENTER);
                if (!lowStock.isEmpty()) {
                    setBackground(sel ? new Color(248,113,113,80) : new Color(248,113,113,25));
                    setForeground(row == 0 && lowStock.isEmpty() ? UIHelper.ACCENT_GREEN : UIHelper.ACCENT_RED);
                } else {
                    setBackground(UIHelper.BG_CARD);
                    setForeground(UIHelper.ACCENT_GREEN);
                }
                return this;
            }
        });

        body.add(UIHelper.scrollPane(table));
        add(body, BorderLayout.CENTER);
    }
}
