package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SellerView extends JFrame {
    private JTable inventoryTable;
    private JButton addProductButton;
    private JButton calculateProfitButton;
    private JButton removeProductButton;
    private JButton editProductButton;

    public SellerView() {
        setTitle("Seller Dashboard");
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(60, 63, 65)); // Dark theme

        // Table for inventory
        inventoryTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Type", "Invoice Price", "Selling Price", "Quantity"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(60, 63, 65)); // Match the theme
        addProductButton = createStyledButton("Add Product");
        calculateProfitButton = createStyledButton("Calculate Profit");
        removeProductButton = createStyledButton("Remove Product");
        editProductButton = createStyledButton("Edit Product");

        buttonPanel.add(addProductButton);
        buttonPanel.add(editProductButton);
        buttonPanel.add(removeProductButton);
        buttonPanel.add(calculateProfitButton);

        // Add components to frame
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 122, 204)); // Blue button
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    public JTable getInventoryTable() {
        return inventoryTable;
    }

    public JButton getAddProductButton() {
        return addProductButton;
    }

    public JButton getCalculateProfitButton() {
        return calculateProfitButton;
    }

    public JButton getRemoveProductButton() {
        return removeProductButton;
    }

    public JButton getEditProductButton() {
        return editProductButton;
    }

    public void addAddProductListener(ActionListener listener) {
        addProductButton.addActionListener(listener);
    }

    public void addCalculateProfitListener(ActionListener listener) {
        calculateProfitButton.addActionListener(listener);
    }

    public void addRemoveProductListener(ActionListener listener) {
        removeProductButton.addActionListener(listener);
    }

    public void addEditProductListener(ActionListener listener) {
        editProductButton.addActionListener(listener);
    }
}
