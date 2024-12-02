package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class CustomerView extends JFrame {
    private JTable productTable;
    private JButton addToCartButton;
    private JButton checkoutButton;
    private JLabel totalLabel;

    public CustomerView() {
        setTitle("Customer Dashboard");
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(60, 63, 65)); // Dark theme

        // Table for product list
        productTable = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Type", "Selling Price", "Quantity"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Panel for buttons and total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(60, 63, 65)); // Match the theme

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(60, 63, 65)); // Match the theme
        addToCartButton = createStyledButton("Add to Cart");
        checkoutButton = createStyledButton("Checkout");

        buttonPanel.add(addToCartButton);
        buttonPanel.add(checkoutButton);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.EAST);

        // Add components to frame
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

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

    public JTable getProductTable() {
        return productTable;
    }

    public JButton getAddToCartButton() {
        return addToCartButton;
    }

    public JButton getCheckoutButton() {
        return checkoutButton;
    }

    public void setTotalLabel(double total) {
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    public void addAddToCartListener(ActionListener listener) {
        addToCartButton.addActionListener(listener);
    }

    public void addCheckoutListener(ActionListener listener) {
        checkoutButton.addActionListener(listener);
    }
}
