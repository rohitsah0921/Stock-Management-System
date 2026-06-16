package stockms.ui;

import stockms.db.SaleDAO;
import stockms.model.Sale;
import stockms.utils.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SalesHistoryPanel extends JPanel {

    private final SaleDAO saleDAO = new SaleDAO();

    public SalesHistoryPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(UIHelper.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Header ────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.BG_DARK);

        JLabel title = UIHelper.heading("  Sales History");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);

        double totalRev = saleDAO.getTotalSalesRevenue();
        double todayRev = saleDAO.getTodaySalesTotal();
        int count = saleDAO.getTotalSalesCount();

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        statsRow.setBackground(UIHelper.BG_DARK);

        statsRow.add(statChip("Total Sales: " + count, UIHelper.ACCENT));
        statsRow.add(statChip("Today: ₹" + String.format("%.2f", todayRev), UIHelper.ACCENT_AMBER));
        statsRow.add(statChip("All Time: ₹" + String.format("%.2f", totalRev), UIHelper.ACCENT_GREEN));

        header.add(title, BorderLayout.WEST);
        header.add(statsRow, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────
        String[] cols = {"Sale ID", "Product ID", "Product Name", "Qty", "Unit Price ₹", "Total ₹", "Customer", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Sale> sales = saleDAO.getAllSales();
        for (Sale s : sales) {
            model.addRow(new Object[]{
                s.getSaleId(), s.getProductId(), s.getProductName(),
                s.getQuantity(),
                String.format("%.2f", s.getUnitPrice()),
                String.format("%.2f", s.getTotalPrice()),
                s.getCustomer(), s.getSaleDate()
            });
        }

        JTable table = new JTable(model);
        UIHelper.styleTable(table);

        // Green color for total column
        table.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(CENTER); }
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, focus, r, c);
                setForeground(UIHelper.ACCENT_GREEN);
                setBackground(UIHelper.BG_CARD);
                return this;
            }
        });

        add(UIHelper.scrollPane(table), BorderLayout.CENTER);
    }

    private JLabel statChip(String text, Color color) {
        JLabel lbl = new JLabel("  " + text + "  ");
        lbl.setForeground(color);
        lbl.setFont(UIHelper.FONT_BODY);
        lbl.setOpaque(true);
        lbl.setBackground(UIHelper.BG_CARD);
        lbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return lbl;
    }
}
