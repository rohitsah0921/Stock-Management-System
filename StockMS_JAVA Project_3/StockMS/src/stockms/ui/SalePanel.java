package stockms.ui;

import stockms.db.ProductDAO;
import stockms.db.SaleDAO;
import stockms.model.Product;
import stockms.model.Sale;
import stockms.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SalePanel extends JPanel {

    private final ProductDAO productDAO = new ProductDAO();
    private final SaleDAO    saleDAO    = new SaleDAO();

    private JComboBox<String> productCombo;
    private JTextField        qtyField, customerField, priceDisplay, totalDisplay;
    private List<Product>     products;

    public SalePanel() {
        setLayout(new GridBagLayout());
        setBackground(UIHelper.BG_DARK);

        JPanel card = UIHelper.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 480));

        JLabel title = UIHelper.heading("  New Sale");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(20));

        // Load products
        products = productDAO.getAllProducts();
        String[] productNames = products.stream()
            .map(p -> p.getProductId() + " — " + p.getName() + " (Stock: " + p.getQuantity() + ")")
            .toArray(String[]::new);

        productCombo  = UIHelper.styledCombo(productNames);
        qtyField      = UIHelper.styledField(); qtyField.setText("1");
        customerField = UIHelper.styledField(); customerField.setText("Walk-in Customer");
        priceDisplay  = UIHelper.styledField(); priceDisplay.setEditable(false);
        totalDisplay  = UIHelper.styledField(); totalDisplay.setEditable(false);
        priceDisplay.setBackground(UIHelper.BG_DARK);
        totalDisplay.setBackground(UIHelper.BG_DARK);
        totalDisplay.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalDisplay.setForeground(UIHelper.ACCENT_GREEN);

        // Auto-fill price when product selected
        productCombo.addActionListener(e -> updatePrice());
        qtyField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePrice(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePrice(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePrice(); }
        });

        addRow(card, "Product *",      productCombo);
        addRow(card, "Unit Price ₹",   priceDisplay);
        addRow(card, "Quantity *",     qtyField);
        addRow(card, "Total ₹",        totalDisplay);
        addRow(card, "Customer Name",  customerField);

        card.add(Box.createVerticalStrut(20));

        JButton sellBtn = UIHelper.successButton("OK  Confirm Sale");
        sellBtn.setAlignmentX(LEFT_ALIGNMENT);
        sellBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        sellBtn.addActionListener(e -> processSale());
        card.add(sellBtn);

        if (!products.isEmpty()) updatePrice();

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

    private void updatePrice() {
        int idx = productCombo.getSelectedIndex();
        if (idx < 0 || idx >= products.size()) return;
        Product p = products.get(idx);
        priceDisplay.setText(String.format("%.2f", p.getUnitPrice()));
        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            totalDisplay.setText(String.format("%.2f", qty * p.getUnitPrice()));
        } catch (NumberFormatException e) {
            totalDisplay.setText("");
        }
    }

    private void processSale() {
        int idx = productCombo.getSelectedIndex();
        if (idx < 0 || idx >= products.size()) {
            error("Please select a product."); return;
        }
        Product p = products.get(idx);

        int qty;
        try {
            qty = Integer.parseInt(qtyField.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Enter a valid quantity (positive integer)."); return;
        }

        if (qty > p.getQuantity()) {
            error("Insufficient stock! Available: " + p.getQuantity()); return;
        }

        double total = qty * p.getUnitPrice();
        String customer = customerField.getText().trim();
        if (customer.isEmpty()) customer = "Walk-in Customer";

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Confirm Sale:\n%s × %d = ₹%.2f\nCustomer: %s",
                p.getName(), qty, total, customer),
            "Confirm Sale", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Sale sale = new Sale(p.getProductId(), p.getName(), qty, p.getUnitPrice(), total, customer);
            if (saleDAO.recordSale(sale)) {
                JOptionPane.showMessageDialog(this,
                    "OK  Sale recorded!\n" + p.getName() + " × " + qty + " = ₹" + String.format("%.2f", total),
                    "Sale Successful", JOptionPane.INFORMATION_MESSAGE);
                // Refresh products list
                products = productDAO.getAllProducts();
                String[] names = products.stream()
                    .map(pr -> pr.getProductId() + " — " + pr.getName() + " (Stock: " + pr.getQuantity() + ")")
                    .toArray(String[]::new);
                productCombo.setModel(new DefaultComboBoxModel<>(names));
                qtyField.setText("1");
                customerField.setText("Walk-in Customer");
                updatePrice();
            } else {
                error("Sale failed. Possibly insufficient stock.");
            }
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
