package stockms.utils;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIHelper {

    // ── Color Palette ──────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(15,  23,  42);   // deep navy
    public static final Color BG_CARD      = new Color(30,  41,  59);   // card bg
    public static final Color BG_INPUT     = new Color(51,  65,  85);   // input bg
    public static final Color ACCENT       = new Color(56, 189, 248);   // sky blue
    public static final Color ACCENT_GREEN = new Color(52, 211, 153);   // emerald
    public static final Color ACCENT_RED   = new Color(248, 113, 113);  // rose
    public static final Color ACCENT_AMBER = new Color(251, 191,  36);  // amber
    public static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    public static final Color TEXT_MUTED   = new Color(148, 163, 184);
    public static final Color BORDER       = new Color(51,  65,  85);

    // ── Fonts ──────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Consolas",  Font.PLAIN, 12);

    public static void setGlobalLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("Panel.background",     BG_DARK);
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
    }

    // ── Styled Components ─────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(BG_DARK);
        btn.setFont(FONT_HEADING);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(ACCENT_RED);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = primaryButton(text);
        btn.setBackground(ACCENT_GREEN);
        btn.setForeground(BG_DARK);
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BG_INPUT);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(FONT_BODY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        return btn;
    }

    public static JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(TEXT_PRIMARY);
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return pf;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_BODY);
        cb.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return cb;
    }

    public static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(FONT_BODY);
        return lbl;
    }

    public static JLabel heading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setFont(FONT_HEADING);
        return lbl;
    }

    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(new Color(56, 189, 248, 60));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER);
        table.setFont(FONT_BODY);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_INPUT);
        header.setForeground(ACCENT);
        header.setFont(FONT_HEADING);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setBackground(BG_CARD);
        center.setForeground(TEXT_PRIMARY);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    public static JScrollPane scrollPane(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(BG_CARD);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return sp;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return p;
    }

    /** Stat card with a big number and a label */
    public static JPanel statCard(String value, String label, Color accent) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18))));

        JLabel valLbl = new JLabel(value);
        valLbl.setForeground(accent);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel nameLbl = new JLabel(label.toUpperCase());
        nameLbl.setForeground(TEXT_MUTED);
        nameLbl.setFont(FONT_SMALL);

        card.add(valLbl, BorderLayout.CENTER);
        card.add(nameLbl, BorderLayout.SOUTH);
        return card;
    }
}
