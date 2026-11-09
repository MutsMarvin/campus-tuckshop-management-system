/*
 * CSC 1102: Structured and Object-Oriented Programming - Practical Exam Project
 * Project : Campus Tuckshop Management System
 * Group   : <GROUP NAME / NUMBER>
 * Members : <Mutabarura Marvin - REG NO>
 *           <NAME  - REG NO>
 *           <NAME  - REG NO>
 *           <NAME  - REG NO>
 *           <NAME  - REG NO>

 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TuckshopApp is the main class. It builds the Swing window (GUI) and
 * reacts to button clicks. All data work is delegated to the Inventory class.
 */
public class TuckshopApp extends JFrame implements ActionListener {

    private Inventory inventory = new Inventory();

    // Stock tab components
    private DefaultTableModel stockModel;
    private JTable stockTable;
    private JComboBox<String> filterBox;
    private JTextField nameField, priceField, qtyField, restockField;
    private JButton addButton, restockButton, removeButton, sortButton;

    // Sell tab components
    private JComboBox<String> productCombo;
    private JTextField sellQtyField;
    private JButton sellButton;
    private JTextArea receiptArea;

    // Sales report tab components
    private DefaultTableModel salesModel;
    private JLabel revenueLabel;

    // Bottom status bar
    private JLabel statusLabel;

    public TuckshopApp() {
        super("Campus Tuckshop Management System");
        inventory.loadSampleData();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Stock", buildStockPanel());
        tabs.addTab("Sell", buildSellPanel());
        tabs.addTab("Sales Report", buildReportPanel());

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(tabs, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        refreshAll();

        setSize(900, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        String[] columns = {"Receipt No", "Item", "Quantity", "Unit Price", "Total"};
        salesModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable salesTable = new JTable(salesModel);

        revenueLabel = new JLabel("Total revenue: UGX 0");
        revenueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));

        panel.add(new JScrollPane(salesTable), BorderLayout.CENTER);
        panel.add(revenueLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshSalesTable() {
        salesModel.setRowCount(0);
        Sale[] list = inventory.getSales();
        for (int i = 0; i < list.length; i++) {
            Sale s = list[i];
            salesModel.addRow(new Object[]{s.getReceiptNo(), s.getProductName(), s.getQuantity(),
                    s.getUnitPrice(), s.getTotal()});
        }
        double average = 0;
        if (list.length > 0) {
            average = inventory.getTotalRevenue() / list.length;
        }
        revenueLabel.setText("Total revenue: UGX " + String.format("%,.0f", inventory.getTotalRevenue())
                + "   |   Items sold: " + inventory.getItemsSold()
                + "   |   Average sale: UGX " + String.format("%,.0f", average));
    }

    // ==================================================================
        // ==================================================================
    private JPanel buildSellPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Product:"));
        productCombo = new JComboBox<String>();
        form.add(productCombo);
        form.add(new JLabel("Quantity:"));
        sellQtyField = new JTextField(4);
        form.add(sellQtyField);
        sellButton = new JButton("Sell");
        sellButton.addActionListener(this);
        form.add(sellButton);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        receiptArea.setText("The receipt will appear here after a sale.");

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        return panel;
    }

    private void handleSell() {
        int index = productCombo.getSelectedIndex();
        if (index == -1) {
            showError("There are no products to sell.");
            return;
        }
        try {
            Product[] all = inventory.getProducts();
            Product chosen = all[index];
            int qty = Integer.parseInt(sellQtyField.getText().trim());

            String error = inventory.validateSale(chosen.getId(), qty);
            if (error != null) {
                showError(error);
                return;
            }
            Sale sale = inventory.sellProduct(chosen.getId(), qty);
            receiptArea.setText(sale.getReceipt());
            sellQtyField.setText("");
            refreshAll();
            updateStatus("Sale completed. Receipt " + sale.getReceiptNo());
        } catch (NumberFormatException ex) {
            showError("Quantity must be a whole number.");
        }
    }

    private void refreshProductCombo() {
        int previous = productCombo.getSelectedIndex();
        productCombo.removeAllItems();
        Product[] all = inventory.getProducts();
        for (int i = 0; i < all.length; i++) {
            productCombo.addItem(all[i].toString());
        }
        if (previous >= 0 && previous < all.length) {
            productCombo.setSelectedIndex(previous);
        }
    }
    

    // ==================================================================
    // >>> MEMBER 5: PASTE YOUR TuckshopApp CODE ON THE LINES BELOW THIS BOX <<<
    // ==================================================================
    // ----- end of Member 5's code -----

    // =====================================================
    //  PROGRAM ENTRY POINT
    // =====================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TuckshopApp().setVisible(true);
            }
        });
    }
}