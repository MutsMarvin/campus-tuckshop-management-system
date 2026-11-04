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

    // ==================================================================
    // >>> MEMBER 2: PASTE YOUR TuckshopApp CODE ON THE LINES BELOW THIS BOX <<<
    // ==================================================================
    // ----- end of Member 2's code -----

    // ==================================================================
    // >>> MEMBER 4: PASTE YOUR TuckshopApp CODE ON THE LINES BELOW THIS BOX <<<
    // ==================================================================
    // ----- end of Member 4's code -----

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