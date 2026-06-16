package stockms.ui;

import stockms.db.ProductDAO;
import stockms.model.Product;
import stockms.utils.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private final ProductDAO dao = new ProductDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    // Form fields
    private JTextField fId, fName, fCategory, fQty, fPrice, fSupplier, fMfg, fExp, fBatch, fAlert;

    public ProductPanel() {
        setLayout(new BorderLayout(14, 0));
        setBackground(UIHelper.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Title bar ─────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIHelper.BG_DARK);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel title = UIHelper.heading("Products / Inventory");
        title.setFont(UIHelper.FONT_TITLE);
        title.setForeground(UIHelper.TEXT_PRIMARY);

        searchField = UIHelper.styledField();
        searchField.setPreferredSize(new Dimension(200, 32));
        searchField.setToolTipText("Search products...");
        JButton searchBtn = UIHelper.ghostButton("Search Search");
        searchBtn.addActionListener(e -> loadData(searchField.getText().trim()));

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        searchRow.setBackground(UIHelper.BG_DARK);
        searchRow.add(UIHelper.label("Search:"));
        searchRow.add(searchField);
        searchRow.add(searchBtn);
        JButton clearBtn = UIHelper.ghostButton("X Clear");
        clearBtn.addActionListener(e -> { searchField.setText(""); loadData(""); });
        searchRow.add(clearBtn);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(searchRow, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────────
        String[] cols = {"ID", "Name", "Category", "Qty", "Unit Price (₹)", "Supplier", "Mfg Date", "Exp Date", "Batch No", "Alert"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIHelper.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> populateFormFromTable());

        add(UIHelper.scrollPane(table), BorderLayout.CENTER);

        // ── Form panel (right) ────────────────────────────────────────
        add(buildForm(), BorderLayout.EAST);

        loadData("");
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIHelper.BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        panel.setPreferredSize(new Dimension(260, 0));

        fId       = UIHelper.styledField();
        fName     = UIHelper.styledField();
        fCategory = UIHelper.styledField();
        fQty      = UIHelper.styledField();
        fPrice    = UIHelper.styledField();
        fSupplier = UIHelper.styledField();
        fMfg      = UIHelper.styledField();
        fExp      = UIHelper.styledField();
        fBatch    = UIHelper.styledField();
        fAlert    = UIHelper.styledField();
        fAlert.setText("10");

        String[][] formFields = {
            {"Product ID *", ""},
            {"Name *", ""},
            {"Category *", "e.g. Medicine"},
            {"Quantity *", ""},
            {"Unit Price ₹ *", ""},
            {"Supplier", ""},
            {"Mfg Date", "DD/MM/YYYY"},
            {"Exp Date", "DD/MM/YYYY"},
            {"Batch No", ""},
            {"Low Stock Alert", ""},
        };
        JTextField[] fields = {fId, fName, fCategory, fQty, fPrice, fSupplier, fMfg, fExp, fBatch, fAlert};

        panel.add(UIHelper.heading("Product Details"));
        panel.add(Box.createVerticalStrut(12));

        for (int i = 0; i < formFields.length; i++) {
            JLabel lbl = UIHelper.label(formFields[i][0]);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            panel.add(lbl);
            panel.add(Box.createVerticalStrut(3));
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            fields[i].setAlignmentX(LEFT_ALIGNMENT);
            panel.add(fields[i]);
            panel.add(Box.createVerticalStrut(8));
        }

        panel.add(Box.createVerticalStrut(8));

        JPanel btnRow = new JPanel(new GridLayout(2, 2, 6, 6));
        btnRow.setBackground(UIHelper.BG_CARD);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        btnRow.setAlignmentX(LEFT_ALIGNMENT);

        JButton addBtn    = UIHelper.successButton("Add");
        JButton updateBtn = UIHelper.primaryButton("Update");
        JButton deleteBtn = UIHelper.dangerButton("Delete");
        JButton clearBtn  = UIHelper.ghostButton("Clear");

        addBtn.addActionListener(e    -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        clearBtn.addActionListener(e  -> clearForm());

        btnRow.add(addBtn);
        btnRow.add(updateBtn);
        btnRow.add(deleteBtn);
        btnRow.add(clearBtn);
        panel.add(btnRow);

        return panel;
    }

    private void addProduct() {
        Product p = readForm();
        if (p == null) return;
        if (dao.productIdExists(p.getProductId())) {
            JOptionPane.showMessageDialog(this, "Product ID already exists!", "Duplicate", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dao.addProduct(p)) {
            showSuccess("Product added successfully!");
            loadData(""); clearForm();
        } else showError("Failed to add product.");
    }

    private void updateProduct() {
        Product p = readForm();
        if (p == null) return;
        if (dao.updateProduct(p)) {
            showSuccess("Product updated!");
            loadData(""); clearForm();
        } else showError("Update failed. Check the Product ID.");
    }

    private void deleteProduct() {
        String id = fId.getText().trim();
        if (id.isEmpty()) { showError("Select a product first."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete product " + id + "? This cannot be undone.", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.deleteProduct(id)) {
                showSuccess("Product deleted.");
                loadData(""); clearForm();
            } else showError("Delete failed.");
        }
    }

    private Product readForm() {
        try {
            String id  = fId.getText().trim();
            String nm  = fName.getText().trim();
            String cat = fCategory.getText().trim();
            String qtyS = fQty.getText().trim();
            String priceS = fPrice.getText().trim();

            if (id.isEmpty() || nm.isEmpty() || cat.isEmpty() || qtyS.isEmpty() || priceS.isEmpty()) {
                showError("Please fill all required (*) fields."); return null;
            }
            int qty = Integer.parseInt(qtyS);
            double price = Double.parseDouble(priceS);
            int alert = fAlert.getText().trim().isEmpty() ? 10 : Integer.parseInt(fAlert.getText().trim());

            return new Product(id, nm, cat, qty, price,
                fSupplier.getText().trim(), fMfg.getText().trim(),
                fExp.getText().trim(), fBatch.getText().trim(), alert);
        } catch (NumberFormatException e) {
            showError("Quantity, Price, and Alert must be numbers."); return null;
        }
    }

    private void populateFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        fId.setText      (str(row, 0));
        fName.setText    (str(row, 1));
        fCategory.setText(str(row, 2));
        fQty.setText     (str(row, 3));
        fPrice.setText   (str(row, 4));
        fSupplier.setText(str(row, 5));
        fMfg.setText     (str(row, 6));
        fExp.setText     (str(row, 7));
        fBatch.setText   (str(row, 8));
        fAlert.setText   (str(row, 9));
    }

    private String str(int row, int col) {
        Object val = model.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        List<Product> list = keyword.isEmpty() ? dao.getAllProducts() : dao.searchProducts(keyword);
        for (Product p : list) {
            model.addRow(new Object[]{
                p.getProductId(), p.getName(), p.getCategory(),
                p.getQuantity(), String.format("%.2f", p.getUnitPrice()),
                p.getSupplier(), p.getMfgDate(), p.getExpDate(),
                p.getBatchNo(), p.getLowStockAlert()
            });
        }
    }

    private void clearForm() {
        fId.setText(""); fName.setText(""); fCategory.setText("");
        fQty.setText(""); fPrice.setText(""); fSupplier.setText("");
        fMfg.setText(""); fExp.setText(""); fBatch.setText(""); fAlert.setText("10");
        table.clearSelection();
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
