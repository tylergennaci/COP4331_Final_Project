package utils;

import model.Customer;
import model.Inventory;
import model.Product;
import model.Seller;
import model.ShoppingCart;
import model.User;

import java.io.*;
import java.util.HashMap;

public class DatabaseManager {
    private static final String USERS_FILE = "users.ser";
    private static final String INVENTORY_FILE = "inventory.ser";

    private static HashMap<String, User> users = new HashMap<>();
    private static Inventory inventory = new Inventory();

    static {
        try {
            loadUsers();
        } catch (Exception e) {
            System.out.println("No users found. Starting with predefined users.");
            createPredefinedUsers();
        }

        try {
            inventory = loadInventory();
        } catch (Exception e) {
            System.out.println("No inventory found. Starting with an empty inventory.");
        }
    }

    public static User getUserByUsername(String username) {
        return users.get(username);
    }

    public static void saveUser(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
    }

    public static Inventory getInventory() {
        return inventory;
    }

    public static void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            System.err.println("Failed to save inventory: " + e.getMessage());
        }
    }

    private static Inventory loadInventory() throws IOException, ClassNotFoundException {
        File file = new File(INVENTORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Inventory) ois.readObject();
            }
        }
        return new Inventory();
    }

    @SuppressWarnings("unchecked")
    private static void loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                users = (HashMap<String, User>) ois.readObject();
            }
        }
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    private static void createPredefinedUsers() {
        users.put("customer", new Customer("customer", "123"));
        users.put("seller", new Seller("seller", "123"));
        saveUsers();
    }

    @SuppressWarnings("unchecked")
    public static void loadCart(Customer customer) {
        File file = new File(customer.getUsername() + "_cart.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                customer.setCart((ShoppingCart) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load cart: " + e.getMessage());
            }
        }
    }

    public static void saveCart(Customer customer) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(customer.getUsername() + "_cart.ser"))) {
            oos.writeObject(customer.getCart());
        } catch (IOException e) {
            System.err.println("Failed to save cart: " + e.getMessage());
        }
    }
}
