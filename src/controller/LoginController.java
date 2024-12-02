package controller;

import view.LoginView;
import view.CustomerView;
import view.SellerView;
import model.User;
import model.Customer;
import model.Seller;
import utils.DatabaseManager;

import javax.swing.*;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.loginView.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        User user = DatabaseManager.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            if (user instanceof Customer) {
                JOptionPane.showMessageDialog(loginView, "Customer logged in!");
                openCustomerView((Customer) user);
            } else if (user instanceof Seller) {
                JOptionPane.showMessageDialog(loginView, "Seller logged in!");
                openSellerView((Seller) user);
            }
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid credentials!");
        }
    }

    private void openCustomerView(Customer customer) {
        CustomerView customerView = new CustomerView();
        new CustomerController(customerView, customer);
        customerView.setVisible(true);
        loginView.dispose();
    }

    private void openSellerView(Seller seller) {
        SellerView sellerView = new SellerView();
        new SellerController(sellerView, seller); // Pass SellerView and Seller
        sellerView.setVisible(true);
        loginView.dispose();
    }
}
