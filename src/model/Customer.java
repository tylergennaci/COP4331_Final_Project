package model;

import java.io.Serializable;

public class Customer extends User implements Serializable {
    private ShoppingCart cart;

    public Customer(String username, String password) {
        super(username, password);
        this.cart = new ShoppingCart();
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
