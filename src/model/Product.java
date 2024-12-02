package model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private String type;
    private int quantity;
    private double invoicePrice;
    private double sellingPrice;
    private int quantitySold = 0;
    private String description; // New field for product description

    public Product(int id, String name, String type, int quantity, double invoicePrice, double sellingPrice, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.invoicePrice = invoicePrice;
        this.sellingPrice = sellingPrice;
        this.description = description; // Initialize description
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(double invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void incrementQuantitySold(int quantity) {
        this.quantitySold += quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
