package controller;

import view.CustomerView;
import view.CheckoutView;
import model.Customer;
import model.Inventory;
import model.Product;
import utils.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerController {
    private CustomerView view;
    private Customer customer;
    private Inventory inventory;

    public CustomerController(CustomerView view, Customer customer) {
        this.view = view;
        this.customer = customer;
        this.inventory = DatabaseManager.getInventory();

        DatabaseManager.loadCart(customer); // Load the customer's cart
        setupProductTable();
        populateProductTable();

        view.getAddToCartButton().addActionListener(e -> addToCart());
        view.getCheckoutButton().addActionListener(e -> checkout());
        view.getProductTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click to view details
                    showProductDetails(view);
                }
            }
        });
    }

    private void setupProductTable() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Type", "Selling Price", "Quantity"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };
        view.getProductTable().setModel(tableModel);
    }

    private void populateProductTable() {
        DefaultTableModel tableModel = (DefaultTableModel) view.getProductTable().getModel();
        tableModel.setRowCount(0); // Clear existing rows

        inventory.getProducts().forEach(product -> {
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getType(),
                    product.getSellingPrice(),
                    product.getQuantity()
            });
        });
    }

    private void showProductDetails(CustomerView customerView) {
        int selectedRow = customerView.getProductTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(customerView, "Please select a product to view details.");
            return;
        }

        String productName = (String) customerView.getProductTable().getValueAt(selectedRow, 1); // Column 1 for name
        Product product = inventory.findProductByName(productName);
        if (product != null) {
            String message = String.format(
                    "Product Details:\n\n" +
                            "Name: %s\n" +
                            "Type: %s\n" +
                            "Selling Price: $%.2f\n" +
                            "Available Quantity: %d\n" +
                            "Description: %s",
                    product.getName(),
                    product.getType(),
                    product.getSellingPrice(),
                    product.getQuantity(),
                    product.getDescription() // Add description here
            );
            JOptionPane.showMessageDialog(customerView, message, "Product Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addToCart() {
        int selectedRow = view.getProductTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Please select a product to add to cart.");
            return;
        }
    
        int productId = (int) view.getProductTable().getValueAt(selectedRow, 0);
        Product product = inventory.findProductById(productId);
        if (product != null && product.getQuantity() > 0) {
            String quantityStr = JOptionPane.showInputDialog(view, "Enter the quantity to add to cart:");
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0 || quantity > product.getQuantity()) {
                    JOptionPane.showMessageDialog(view, "Invalid quantity. Please enter a number between 1 and " + product.getQuantity() + ".");
                    return;
                }
    
                customer.getCart().addProduct(product, quantity);
                product.setQuantity(product.getQuantity() - quantity); // Update inventory quantity
                product.incrementQuantitySold(quantity); // Increment quantitySold for profit calculation
                DatabaseManager.saveInventory();
                DatabaseManager.saveCart(customer);
                JOptionPane.showMessageDialog(view, "Added " + quantity + " " + product.getName() + "(s) to cart.");
                view.setTotalLabel(customer.getCart().calculateTotal());
                populateProductTable(); // Refresh table
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid input. Please enter a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Product is out of stock.");
        }
    }

    private void checkout() {
        CheckoutView checkoutView = new CheckoutView();
        populateCheckoutTable(checkoutView);

        checkoutView.getUpdateQuantityButton().addActionListener(e -> updateCartItem(checkoutView));
        checkoutView.getRemoveItemButton().addActionListener(e -> removeCartItem(checkoutView));
        checkoutView.getProceedToPaymentButton().addActionListener(e -> processPayment(checkoutView));

        checkoutView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshCustomerDashboard(); // Refresh dashboard when the checkout window is closed
            }
        });

        checkoutView.getCartTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click to view product details
                    showProductDetails(view); // Correct argument
                }
            }
        });

        checkoutView.setVisible(true);
    }

    private void populateCheckoutTable(CheckoutView checkoutView) {
        DefaultTableModel tableModel = (DefaultTableModel) checkoutView.getCartTable().getModel();
        tableModel.setRowCount(0); // Clear existing rows

        customer.getCart().getCartItems().forEach((product, quantity) -> {
            double subtotal = product.getSellingPrice() * quantity;
            tableModel.addRow(new Object[]{
                    product.getName(),
                    product.getSellingPrice(),
                    quantity,
                    subtotal
            });
        });

        checkoutView.setTotalLabel(customer.getCart().calculateTotal());
    }

    private void updateCartItem(CheckoutView checkoutView) {
        int selectedRow = checkoutView.getCartTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(checkoutView, "Please select a product to update.");
            return;
        }

        String productName = (String) checkoutView.getCartTable().getValueAt(selectedRow, 0); // Column 0 for name
        Product product = inventory.findProductByName(productName);
        if (product != null) {
            String quantityStr = JOptionPane.showInputDialog(checkoutView, "Enter the new quantity:");
            try {
                int newQuantity = Integer.parseInt(quantityStr);
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(checkoutView, "Invalid quantity. Use 'Remove Item' to delete.");
                    return;
                }

                int currentCartQuantity = customer.getCart().getCartItems().get(product);
                product.setQuantity(product.getQuantity() + currentCartQuantity - newQuantity); // Update inventory
                customer.getCart().updateProductQuantity(product, newQuantity); // Update cart
                DatabaseManager.saveInventory();
                DatabaseManager.saveCart(customer);
                JOptionPane.showMessageDialog(checkoutView, "Cart updated successfully.");
                populateCheckoutTable(checkoutView); // Refresh checkout table
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(checkoutView, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private void removeCartItem(CheckoutView checkoutView) {
        int selectedRow = checkoutView.getCartTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(checkoutView, "Please select a product to remove.");
            return;
        }

        String productName = (String) checkoutView.getCartTable().getValueAt(selectedRow, 0); // Column 0 for name
        Product product = inventory.findProductByName(productName);
        if (product != null) {
            int currentCartQuantity = customer.getCart().getCartItems().get(product);
            product.setQuantity(product.getQuantity() + currentCartQuantity); // Return to inventory
            customer.getCart().removeProduct(product); // Remove from cart
            DatabaseManager.saveInventory();
            DatabaseManager.saveCart(customer);
            JOptionPane.showMessageDialog(checkoutView, "Product removed from cart.");
            populateCheckoutTable(checkoutView); // Refresh checkout table
        }
    }

    private void processPayment(CheckoutView checkoutView) {
        double total = customer.getCart().calculateTotal();
        if (total > 0) {
            String creditCardNumber = JOptionPane.showInputDialog(checkoutView, "Enter Credit Card Number:");
            if (creditCardNumber != null && !creditCardNumber.isEmpty()) {
                customer.getCart().clearCart(); // Clear the cart
                DatabaseManager.saveCart(customer);
                JOptionPane.showMessageDialog(checkoutView, "Payment successful! Total: $" + total);
                checkoutView.dispose(); // Close the checkout window
            } else {
                JOptionPane.showMessageDialog(checkoutView, "Payment canceled.");
            }
        } else {
            JOptionPane.showMessageDialog(checkoutView, "Your cart is empty.");
        }
    }

    private void refreshCustomerDashboard() {
        view.setTotalLabel(customer.getCart().calculateTotal());
        populateProductTable(); // Refresh product table
    }
}
