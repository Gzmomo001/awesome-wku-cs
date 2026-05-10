package payment;

public class Payment {
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount);
    }

    public void CreditCardPayment(double amount) {
        System.out.println("Payment of $" + amount + " made via Credit Card.");
    }

    public void PayPalPayment(double amount) {
        System.out.println("Payment of $" + amount + " made via PayPal.");
    }
}
