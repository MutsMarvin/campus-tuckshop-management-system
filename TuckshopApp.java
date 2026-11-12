/*
 * CSC 1102: Structured and Object-Oriented Programming - Practical Exam Project
 * Project : Campus Tuckshop Management System
 * Group   : <GROUP 9
 * Members : <Mutabarura Marvin - REG NO>
 *           <WASSWA SIMON SSEMWOGERERE	25/U/03612/PS
 *           <SSENTONGO JOSHUA	26/U/03646/PSA
 *           <AKATUKUNDA PERINAH	26/U/03477/PSA
 *           <SSEBULIBA JOEL NICHOLAS	26/U/31010/PSA

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
    

    private JTextField searchField, categoryField;
    private JButton searchButton, sortNameButton;

    private JPanel buildStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // --- Top: filter and sort ---
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Show:"));
        filterBox = new JComboBox<String>(new String[]{"All products", "Low stock", "Out of stock"});
        filterBox.addActionListener(this);
        top.add(filterBox);
        sortButton = new JButton("Sort by Price");
        sortButton.addActionListener(this);
        top.add(sortButton);
        sortNameButton = new JButton("Sort by Name");
        sortNameButton.addActionListener(this);
        top.add(sortNameButton);
        top.add(new JLabel("Search:"));
        searchField = new JTextField(10);
        top.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        top.add(searchButton);

        // --- Centre: table of products ---
        String[] columns = {"ID", "Name", "Category", "Price (UGX)", "Quantity", "Status"};
        stockModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;               // user cannot type into the table
            }
        };
        stockTable = new JTable(stockModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Bottom: add / restock / remove forms ---
        JPanel bottom = new JPanel(new GridLayout(2, 1, 4, 4));

        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRow.add(new JLabel("Name:"));
        nameField = new JTextField(12);
        addRow.add(nameField);
        addRow.add(new JLabel("Price:"));
        priceField = new JTextField(6);
        addRow.add(priceField);
        addRow.add(new JLabel("Qty:"));
        qtyField = new JTextField(4);
        addRow.add(qtyField);
        addRow.add(new JLabel("Category:"));
        categoryField = new JTextField(8);
        addRow.add(categoryField);
        addButton = new JButton("Add Product");
        addButton.addActionListener(this);
        addRow.add(addButton);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionRow.add(new JLabel("Select a row, then:  Add stock:"));
        restockField = new JTextField(4);
        actionRow.add(restockField);
        restockButton = new JButton("Restock");
        restockButton.addActionListener(this);
        actionRow.add(restockButton);
        removeButton = new JButton("Remove Product");
        removeButton.addActionListener(this);
        actionRow.add(removeButton);

        bottom.add(addRow);
        bottom.add(actionRow);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();      // which component was clicked?

        if (source == addButton) {
            handleAdd();
        } else if (source == restockButton) {
            handleRestock();
        } else if (source == removeButton) {
            handleRemove();
        } else if (source == sortButton) {
            inventory.sortByPrice();
            refreshAll();
            updateStatus("Products sorted by price (cheapest first).");
        } else if (source == sortNameButton) {
            inventory.sortByName();
            refreshAll();
            updateStatus("Products sorted by name (A to Z).");
        } else if (source == searchButton) {
            refreshStockTable();
        } else if (source == filterBox) {
            refreshStockTable();
        } else if (source == sellButton) {
            handleSell();
        }
    }

    private void handleAdd() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());

            Product p = inventory.addProduct(name, price, qty);
            if (p == null) {
                showError("Enter a name, a price above 0, and a quantity of 0 or more.");
            } else {
                p.setCategory(categoryField.getText());
                nameField.setText("");
                categoryField.setText("");
                priceField.setText("");
                qtyField.setText("");
                refreshAll();
                updateStatus("Added: " + p.getName());
            }
        } catch (NumberFormatException ex) {
            showError("Price and quantity must be numbers.");
        }
    }

    private void handleRestock() {
        int row = stockTable.getSelectedRow();
        if (row == -1) {
            showError("Please select a product in the table first.");
            return;
        }
        try {
            int id = (Integer) stockModel.getValueAt(row, 0);
            int amount = Integer.parseInt(restockField.getText().trim());
            if (inventory.restock(id, amount)) {
                restockField.setText("");
                refreshAll();
                updateStatus("Stock updated.");
            } else {
                showError("Amount must be at least 1.");
            }
        } catch (NumberFormatException ex) {
            showError("Enter a whole number to add.");
        }
    }

    private void handleRemove() {
        int row = stockTable.getSelectedRow();
        if (row == -1) {
            showError("Please select a product in the table first.");
            return;
        }
        int id = (Integer) stockModel.getValueAt(row, 0);
        String name = (String) stockModel.getValueAt(row, 1);

        int choice = JOptionPane.showConfirmDialog(this,
                "Remove \"" + name + "\" from the shop?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            inventory.removeProduct(id);
            refreshAll();
            updateStatus("Removed: " + name);
        }
    }

    private void refreshAll() {
        refreshStockTable();
        refreshProductCombo();
        refreshSalesTable();
        updateStatus("Ready.");
    }

    private void refreshStockTable() {
        stockModel.setRowCount(0);                          // clear the table
        String keyword = searchField.getText().trim();
        Product[] list;
        if (keyword.isEmpty()) {
            list = inventory.filterProducts((String) filterBox.getSelectedItem());
        } else {
            list = inventory.findByName(keyword);       // Member 3's search method
        }
        for (int i = 0; i < list.length; i++) {
            Product p = list[i];
            stockModel.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getQuantity(), p.getStatus()});
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Shows a message in the status bar, followed by a low-stock warning if needed. */
    private void updateStatus(String message) {
        int low = inventory.countLowStock();
        if (low > 0) {
            message = message + "   |   Warning: " + low + " product(s) low or out of stock.";
        }
        statusLabel.setText(message);
    }

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
