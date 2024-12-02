package tests;

import model.Product;
import model.ShoppingCart;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShoppingCartTest {

    @Test
    public void testAddProduct() {
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product(1, "Laptop", "Electronics", 1, 500, 700);
        cart.addProduct(product, 2);

        assertEquals(2, cart.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testCalculateTotal() {
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product(1, "Laptop", "Electronics", 2, 500, 700);
        cart.addProduct(product, 2);

        assertEquals(1400, cart.calculateTotal(), 0);
    }
}
