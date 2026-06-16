package stockms.ui;

import stockms.db.ProductDAO;
import stockms.db.PurchaseDAO;
import stockms.model.Product;
import stockms.model.Purchase;
import stockms.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PurchasePanel extends JPanel {

    private final ProductDAO  productDAO  = new ProductDAO();
    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    private JComboBox<String> productCombo;
    private JTextField qtyField, costField, supplierField, totalDisplay;
    private List<Product> products;

    public PurchasePanel() {
        setLayout(new GridBagLayout());
        setBackground(UIHelper.BG_DARK);

        JPanel card = UIHelper.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 420));

        JLabel title = UIHelper.heading("  Record Purchase / Restock");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        products = productDAO.getAllProducts();
        String[] names = products.stream()
            .map(p -> p.getProductId() + " — " + p.getName())
            .toArray(String[]::new);

        productCombo = UIHelper.styledCombo(names);
        qtyField     = UIHelper.styledField(); qtyField.setText("1");
        costField    = UIHelper.styledField(); costField.setText("0");
        supplierField= UIHelper.styledField();
        totalDisplay = UIHelper.styledField(); totalDisplay.setEditable(false);
        totalDisplay.setBackground(UIHelper.BG_DARK);
        totalDisplay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalDisplay.setForeground(UIHelper.ACCENT_AMBER);

        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcTotal(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcTotal(); }
        };
        qtyField.getDocument().addDocumentListener(dl);
        costField.getDocument().addDocumentListener(dl);

        addRow(card, "Product *",        productCombo);
        addRow(card, "Quantity *",       qtyField);
        addRow(card, "Unit Cost ₹ *",    costField);
        addRow(card, "Total Cost ₹",     totalDisplay);
        addRow(card, "Supplier",         supplierField);

        card.add(Box.createVerticalStrut(20));

        JButton btn = UIHelper.primaryButton("OK  Record Purchase");
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.addActionListener(e -> recordPurchase());
        card.add(btn);

        calcTotal();
        add(card);
    }

    private void addRow(JPanel card, String labelText, JComponent field) {
        JLabel lbl = UIHelper.label(labelText);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(4));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(LEFT_ALIGNMENT);
        card.add(field);
        card.add(Box.createVerticalStrut(12));
    }

    private void calcTotal() {
        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            double cost = Double.parseDouble(costField.getText().trim());
            totalDisplay.setText(String.format("%.2f", qty * cost));
        } catch (NumberFormatException e) {
            totalDisplay.setText("");
        }
    }

    private void recordPurchase() {
        int idx = productCombo.getSelectedIndex();
        if (idx < 0 || idx >= products.size()) { error("Select a product."); return; }
        Product p = products.get(idx);

        int qty; double cost;
        try {
            qty = Integer.parseInt(qtyField.getText().trim());
            cost = Double.parseDouble(costField.getText().trim());
            if (qty <= 0 || cost < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Enter valid Quantity and Cost."); return;
        }

        double total = qty * cost;
        String supplier = supplierField.getText().trim();
        if (supplier.isEmpty()) supplier = p.getSupplier() != null ? p.getSupplier() : "Unknown";

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Record Purchase:\n%s × %d @ ₹%.2f = ₹%.2f\nSupplier: %s",
                p.getName(), qty, cost, total, supplier),
            "Confirm Purchase", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Purchase purchase = new Purchase(p.getProductId(), p.getName(), qty, cost, total, supplier);
            if (purchaseDAO.recordPurchase(purchase)) {
                JOptionPane.showMessageDialog(this,
                    "OK  Purchase recorded! Stock updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                qtyField.setText("1"); costField.setText("0"); supplierField.setText("");
            } else {
                error("Failed to record purchase.");
            }
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
