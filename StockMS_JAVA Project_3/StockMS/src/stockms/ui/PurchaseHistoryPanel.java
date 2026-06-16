package stockms.ui;

import stockms.db.PurchaseDAO;
import stockms.model.Purchase;
import stockms.utils.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PurchaseHistoryPanel extends JPanel {

    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    public PurchaseHistoryPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(UIHelper.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.BG_DARK);

        JLabel title = UIHelper.heading("  Purchase History");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);

        double totalCost = purchaseDAO.getTotalPurchaseCost();
        JLabel costLabel = new JLabel("Total Purchased: ₹" + String.format("%.2f", totalCost));
        costLabel.setForeground(UIHelper.ACCENT_AMBER);
        costLabel.setFont(UIHelper.FONT_BODY);

        header.add(title, BorderLayout.WEST);
        header.add(costLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"Purchase ID", "Product ID", "Product Name", "Qty", "Unit Cost ₹", "Total Cost ₹", "Supplier", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Purchase> purchases = purchaseDAO.getAllPurchases();
        for (Purchase p : purchases) {
            model.addRow(new Object[]{
                p.getPurchaseId(), p.getProductId(), p.getProductName(),
                p.getQuantity(),
                String.format("%.2f", p.getUnitCost()),
                String.format("%.2f", p.getTotalCost()),
                p.getSupplier(), p.getPurchaseDate()
            });
        }

        JTable table = new JTable(model);
        UIHelper.styleTable(table);

        table.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(CENTER); }
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, focus, r, c);
                setForeground(UIHelper.ACCENT_AMBER);
                setBackground(UIHelper.BG_CARD);
                return this;
            }
        });

        add(UIHelper.scrollPane(table), BorderLayout.CENTER);
    }
}
