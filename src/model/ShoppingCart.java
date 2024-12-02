package model;

import java.io.Serializable;
import java.util.HashMap;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L; // Add serialVersionUID

    private HashMap<Product, Integer> cartItems;

    public ShoppingCart() {
        this.cartItems = new HashMap<>();
    }

    public HashMap<Product, Integer> getCartItems() {
        return cartItems;
    }

    public void addProduct(Product product, int quantity) {
        cartItems.put(product, cartItems.getOrDefault(product, 0) + quantity);
    }

    public void updateProductQuantity(Product product, int newQuantity) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, newQuantity);
        }
    }

    public void removeProduct(Product product) {
        cartItems.remove(product);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double calculateTotal() {
        return cartItems.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getSellingPrice() * entry.getValue())
                .sum();
    }
}
