package model;

import payment.Payment;

public class Booking {
    private Flight flight;
    private Passenger passenger;
    private Payment payment;
    private double price;

    public Booking(Flight flight, Passenger passenger, Payment payment) {
        this.flight = flight;
        this.passenger = passenger;
        this.payment = payment;
    }

    public String getSummary() {
        return passenger.getName() + " booked " + flight.getDetails();
    }

    public void confirm(double amount) {
        this.price = amount;
        payment.processPayment(amount);
        System.out.println("Booking confirmed for " + passenger.getName());
    }

    // Getter methods for database storage
    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getPaymentMethod() {
        return payment.getClass().getSimpleName().replace("Payment", "");
    }

    public double getPrice() {
        return price;
    }
}
