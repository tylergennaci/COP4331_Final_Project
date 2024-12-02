package controller;

import view.SellerView;
import model.Inventory;
import model.Product;
import model.Seller;
import utils.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SellerController {
    private SellerView view;
    private Seller seller;
    private Inventory inventory;

    public SellerController(SellerView view, Seller seller) {
        this.view = view;
        this.seller = seller;
        this.inventory = DatabaseManager.getInventory();

        populateInventoryTable();
        view.getAddProductButton().addActionListener(e -> addProduct());
        view.getCalculateProfitButton().addActionListener(e -> calculateProfit());
        view.getRemoveProductButton().addActionListener(e -> removeProduct());
        view.getEditProductButton().addActionListener(e -> editProduct());
    }

    private void populateInventoryTable() {
        DefaultTableModel tableModel = (DefaultTableModel) view.getInventoryTable().getModel();
        tableModel.setRowCount(0);

        for (Product product : inventory.getProducts()) {
            tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getType(),
                    product.getInvoicePrice(),
                    product.getSellingPrice(),
                    product.getQuantity()
            });
        }
    }

    private void addProduct() {
        String name = JOptionPane.showInputDialog(view, "Enter product name:");
        String type = JOptionPane.showInputDialog(view, "Enter product type:");
        String description = JOptionPane.showInputDialog(view, "Enter product description:"); // New input for description
        String invoicePriceStr = JOptionPane.showInputDialog(view, "Enter invoice price:");
        String sellingPriceStr = JOptionPane.showInputDialog(view, "Enter selling price:");
        String quantityStr = JOptionPane.showInputDialog(view, "Enter quantity:");

        if (name != null && type != null && description != null && invoicePriceStr != null && sellingPriceStr != null && quantityStr != null) {
            try {
                double invoicePrice = Double.parseDouble(invoicePriceStr);
                double sellingPrice = Double.parseDouble(sellingPriceStr);
                int quantity = Integer.parseInt(quantityStr);
                int id = inventory.getProducts().size() + 1;

                Product newProduct = new Product(id, name, type, quantity, invoicePrice, sellingPrice, description);
                inventory.addProduct(newProduct);

                DatabaseManager.saveInventory();
                populateInventoryTable();
                JOptionPane.showMessageDialog(view, "Product added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Invalid input. Please enter valid numbers.");
            }
        }
    }

    private void calculateProfit() {
        double revenue = inventory.getProducts().stream()
                .mapToDouble(p -> p.getSellingPrice() * p.getQuantitySold()) // Total revenue
                .sum();
        double cost = inventory.getProducts().stream()
                .mapToDouble(p -> p.getInvoicePrice() * p.getQuantitySold()) // Total cost
                .sum();
        double profit = revenue - cost; // Profit = Revenue - Cost
    
        JOptionPane.showMessageDialog(view, "Total Profit: $" + String.format("%.2f", profit));
    }

    private void removeProduct() {
        int selectedRow = view.getInventoryTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Please select a product to remove.");
            return;
        }

        int productId = (int) view.getInventoryTable().getValueAt(selectedRow, 0);
        inventory.removeProduct(productId);
        DatabaseManager.saveInventory();
        populateInventoryTable();
        JOptionPane.showMessageDialog(view, "Product removed successfully!");
    }

    private void editProduct() {
        int selectedRow = view.getInventoryTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Please select a product to edit.");
            return;
        }

        int productId = (int) view.getInventoryTable().getValueAt(selectedRow, 0);
        Product product = inventory.findProductById(productId);
        if (product != null) {
            String name = JOptionPane.showInputDialog(view, "Enter new name:", product.getName());
            String type = JOptionPane.showInputDialog(view, "Enter new type:", product.getType());
            String description = JOptionPane.showInputDialog(view, "Enter new description:", product.getDescription()); // Update description
            String invoicePriceStr = JOptionPane.showInputDialog(view, "Enter new invoice price:", product.getInvoicePrice());
            String sellingPriceStr = JOptionPane.showInputDialog(view, "Enter new selling price:", product.getSellingPrice());
            String quantityStr = JOptionPane.showInputDialog(view, "Enter new quantity:", product.getQuantity());

            if (name != null && type != null && description != null && invoicePriceStr != null && sellingPriceStr != null && quantityStr != null) {
                try {
                    double invoicePrice = Double.parseDouble(invoicePriceStr);
                    double sellingPrice = Double.parseDouble(sellingPriceStr);
                    int quantity = Integer.parseInt(quantityStr);

                    product.setName(name);
                    product.setType(type);
                    product.setDescription(description); // Update description
                    product.setInvoicePrice(invoicePrice);
                    product.setSellingPrice(sellingPrice);
                    product.setQuantity(quantity);

                    DatabaseManager.saveInventory();
                    populateInventoryTable();
                    JOptionPane.showMessageDialog(view, "Product updated successfully!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(view, "Invalid input. Please enter valid numbers.");
                }
            }
        }
    }
}
