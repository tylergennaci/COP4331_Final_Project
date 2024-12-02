package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckoutView extends JFrame {
    private JTable cartTable;
    private JButton updateQuantityButton;
    private JButton removeItemButton;
    private JButton proceedToPaymentButton;
    private JLabel totalLabel;

    public CheckoutView() {
        setTitle("Checkout");
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(60, 63, 65)); // Dark theme

        // Table for cart items
        cartTable = new JTable(new DefaultTableModel(
                new Object[]{"Product Name", "Price", "Quantity", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(cartTable);

        // Panel for buttons and total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(60, 63, 65)); // Match the theme

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(60, 63, 65)); // Match the theme
        updateQuantityButton = createStyledButton("Update Quantity");
        removeItemButton = createStyledButton("Remove Item");
        proceedToPaymentButton = createStyledButton("Proceed to Payment");

        buttonPanel.add(updateQuantityButton);
        buttonPanel.add(removeItemButton);
        buttonPanel.add(proceedToPaymentButton);

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

    public JTable getCartTable() {
        return cartTable;
    }

    public JButton getUpdateQuantityButton() {
        return updateQuantityButton;
    }

    public JButton getRemoveItemButton() {
        return removeItemButton;
    }

    public JButton getProceedToPaymentButton() {
        return proceedToPaymentButton;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public void setTotalLabel(double total) {
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    public void addUpdateQuantityListener(ActionListener listener) {
        updateQuantityButton.addActionListener(listener);
    }

    public void addRemoveItemListener(ActionListener listener) {
        removeItemButton.addActionListener(listener);
    }

    public void addProceedToPaymentListener(ActionListener listener) {
        proceedToPaymentButton.addActionListener(listener);
    }
}
