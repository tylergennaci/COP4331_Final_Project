package utils;

/**
 * Utility class to simulate the credit card payment process.
 */
public class PaymentProcessor {
    /**
     * Simulates the credit card payment process.
     *
     * @param creditCardNumber The credit card number (as a string).
     * @param amount The amount to be charged (as a double).
     * @return true if the payment is successful.
     */
    public static boolean processPayment(String creditCardNumber, double amount) {
        // Simulate a successful payment process
        System.out.println("Processing payment...");
        System.out.println("Credit Card Number: " + creditCardNumber);
        System.out.println("Amount: $" + amount);
        System.out.println("Payment successful!");
        return true; // Assume all payments are successful
    }
}
