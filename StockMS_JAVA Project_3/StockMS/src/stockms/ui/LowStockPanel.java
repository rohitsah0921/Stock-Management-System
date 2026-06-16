package stockms.ui;

import stockms.db.ProductDAO;
import stockms.model.Product;
import stockms.utils.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LowStockPanel extends JPanel {

    private final ProductDAO productDAO = new ProductDAO();

    public LowStockPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(UIHelper.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = UIHelper.heading("(!)  Low Stock Alerts");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.ACCENT_RED);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Product ID", "Name", "Category", "In Stock", "Alert Level", "Supplier", "Exp Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Product> list = productDAO.getLowStockProducts();

        if (list.isEmpty()) {
            JLabel ok = new JLabel("OK  All products have sufficient stock!", SwingConstants.CENTER);
            ok.setForeground(UIHelper.ACCENT_GREEN);
            ok.setFont(new Font("Segoe UI", Font.BOLD, 18));
            add(ok, BorderLayout.CENTER);
            return;
        }

        for (Product p : list) {
            model.addRow(new Object[]{
                p.getProductId(), p.getName(), p.getCategory(),
                p.getQuantity(), p.getLowStockAlert(),
                p.getSupplier(), p.getExpDate()
            });
        }

        JTable table = new JTable(model);
        UIHelper.styleTable(table);

        // Color rows based on severity
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(CENTER);
                int stock = list.get(row).getQuantity();
                int alert = list.get(row).getLowStockAlert();
                if (stock == 0) {
                    setBackground(sel ? new Color(248,113,113,100) : new Color(248,113,113,40));
                    setForeground(UIHelper.ACCENT_RED);
                } else if (stock <= alert / 2) {
                    setBackground(sel ? new Color(251,191,36,100) : new Color(251,191,36,25));
                    setForeground(UIHelper.ACCENT_AMBER);
                } else {
                    setBackground(UIHelper.BG_CARD);
                    setForeground(UIHelper.TEXT_PRIMARY);
                }
                return this;
            }
        });

        JLabel note = new JLabel("  " + list.size() + " product(s) need restocking  |  🔴 Out of stock  🟡 Critically low");
        note.setForeground(UIHelper.TEXT_MUTED);
        note.setFont(UIHelper.FONT_SMALL);
        note.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        add(UIHelper.scrollPane(table), BorderLayout.CENTER);
        add(note, BorderLayout.SOUTH);
    }
}
