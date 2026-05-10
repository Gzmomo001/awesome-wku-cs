package DatabaseManagement;

import crawler.FlightData;
import crawler.FlightRouteGenerator;
import model.Booking;
import model.Flight;
import model.Passenger;
import model.DomesticFlight;
import model.InternationalFlight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all MySQL database operations for the flight booking
 * system.
 * Manages connections, creates schema, and provides methods for CRUD
 * operations.
 */
public class DatabaseManager {
    // login as 'flight_booking_user'
    // login info can vary according to user network. Implement AddressUtils.java
    // for future use
    // 10.36.48.245 is ip on wku-vpn wifi

    private static final String DB_URL = "jdbc:mysql://10.36.48.245:3306/flight_booking";
    private static final String DB_USER = "flight_booking_user";
    private static final String DB_PASSWORD = "2231cps";

    private Connection connection;

    /*
     * Constructor establishes database connection and initializes schema
     */
    public DatabaseManager() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println(" > Connected to database successfully <");

            // Create tables if they don't exist
            createTablesIfNotExist();

        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException es) {
            System.err.println("[ERROR] Database connection failed!");
            System.err.println("Make sure MySQL is running and the 'flight_booking' database exists.");
            System.err.println("You can create it with: CREATE DATABASE flight_booking;");
            es.printStackTrace();
        }
    }

    /*
     * Creates database tables if they don't already exist
     */
    private void createTablesIfNotExist() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS bookings (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  passenger_name VARCHAR(255) NOT NULL," +
                "  passport_number VARCHAR(50) NOT NULL," +
                "  phone_number VARCHAR(50) NOT NULL," +
                "  flight_type VARCHAR(50) NOT NULL," +
                "  flight_number VARCHAR(20) NOT NULL," +
                "  origin VARCHAR(100) NOT NULL," +
                "  destination VARCHAR(100) NOT NULL," +
                "  departure_date DATE NOT NULL," +
                "  departure_time TIME NOT NULL," +
                "  arrival_time TIME NOT NULL," +
                "  payment_method VARCHAR(50) NOT NULL," +
                "  price Double NOT NULL," +
                "  booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("[Info] Database tables ready");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to create tables");
            e.printStackTrace();
        }
    }

    /**
     * Insert a new booking into the database
     * 
     * @param booking The booking to save
     * @return true if insertion was successful, false otherwise
     */
    public boolean insertBooking(Booking booking) {
        String insertSQL = "INSERT INTO bookings (passenger_name, passport_number, phone_number, " +
                "flight_type, flight_number, " +
                "origin, destination, departure_date, departure_time, arrival_time, " +
                "payment_method, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            Flight flight = booking.getFlight();
            Passenger passenger = booking.getPassenger();
            // Use actual flight details instead of empty FlightData
            String airline = "";
            if (flight instanceof DomesticFlight) {
                airline = ((DomesticFlight) flight).getAirline();
            } else if (flight instanceof InternationalFlight) {
                airline = "International"; // placeholder or could be derived elsewhere
            }
            // Assuming price is stored in booking
            double price = booking.getPrice();

            pstmt.setString(1, passenger.getName());
            pstmt.setString(2, passenger.getPassportNumber());
            pstmt.setString(3, passenger.getPhoneNumber());
            pstmt.setString(4, flight instanceof DomesticFlight ? "Domestic" : "International");
            // Set parameters matching the INSERT columns
            pstmt.setString(1, passenger.getName());
            pstmt.setString(2, passenger.getPassportNumber());
            pstmt.setString(3, passenger.getPhoneNumber());
            pstmt.setString(4, flight instanceof DomesticFlight ? "Domestic" : "International");
            pstmt.setString(5, airline);
            pstmt.setString(6, flight.getOrigin());
            pstmt.setString(7, flight.getDestination());
            pstmt.setDate(8, Date.valueOf(flight.getDepartureDate()));
            pstmt.setTime(9, Time.valueOf(flight.getDepartureTime()));
            pstmt.setTime(10, Time.valueOf(flight.getArrivalTime()));
            pstmt.setString(11, booking.getPaymentMethod());
            pstmt.setDouble(12, booking.getPrice());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert booking");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieve all bookings from the database
     * 
     * @return List of booking summaries as strings
     */
    public List<String> getAllBookings() {
        List<String> bookings = new ArrayList<>();
        String selectSQL = "SELECT * FROM bookings ORDER BY id ASC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String summary = String.format(
                        "[#%d] %s (Passport: %s, Phone: %s)\n" +
                                "Flight Type: %s  Flight Number: %s\n" +
                                "Origin: %s ---> Destination: %s\n" +
                                "Departure Date: %s\n" +
                                "Time: %s : %s\n" +
                                "Payment: (%s) %.2f\n " +
                                "Booking Date: %s\n\n",
                        rs.getInt("id"),
                        rs.getString("passenger_name"),
                        rs.getString("passport_number"),
                        rs.getString("phone_number"),
                        rs.getString("flight_type"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getDate("departure_date"),
                        rs.getTime("departure_time"),
                        rs.getTime("arrival_time"),
                        rs.getString("payment_method"),
                        rs.getDouble("price"),
                        rs.getTimestamp("booking_date"));

                bookings.add(summary);
            }

        } catch (SQLException e) {
            System.err.println("[Error] Failed to retrieve bookings");
            e.printStackTrace();
        }

        return bookings;
    }

    /**
     * Retrieve one booking from the database(based on phone_number)
     *
     * @return List of individual booking summaries as strings (No id, passport,
     *         phone or url)
     */
    public List<String> getPersonalBooking(String phoneNum) {
        List<String> bookingPersonal = new ArrayList<>();
        String selectSQL = "SELECT passenger_name, flight_type, flight_number, origin, destination, " +
                "departure_date, departure_time, arrival_time, " +
                "payment_method, price, booking_date" +
                " FROM bookings WHERE phone_number = " + phoneNum;

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String summary = String.format(
                        "Name: %s\n" +
                                "Flight Type: %s  Flight Number: %s\n" +
                                "Origin: %s ---> Destination: %s\n" +
                                "Departure Date: %s\n" +
                                "Time: %s : %s\n" +
                                "Payment: (%s) %.2f\n" +
                                "Booking Date: %s\n",
                        rs.getString("passenger_name"),
                        rs.getString("flight_type"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getDate("departure_date"),
                        rs.getTime("departure_time"),
                        rs.getTime("arrival_time"),
                        rs.getString("payment_method"),
                        rs.getDouble("price"),
                        rs.getTimestamp("booking_date"));

                bookingPersonal.add(summary);
            }

        } catch (SQLException e) {
            System.err.println("[Error] Failed to retrieve bookings");
            e.printStackTrace();
        }

        return bookingPersonal;
    }

    /**
     * Update a specific value of a key in bookings(table)
     *
     * Parameter variable (id)
     */
    public void updateValueBookings(int id, String key, String changedValue) {
        try {
            String updateStmt = "UPDATE bookings SET " + key + " = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateStmt);
            pstmt.setString(1, changedValue);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            confirmUpdate(id);

        } catch (SQLException e) {
            System.out.println("[Error] Failure to update");
            e.printStackTrace();
        }
    }

    // Confirm Update: print out the updated row
    public void confirmUpdate(int id) {
        String confirmUpdateStmt = "Select * FROM bookings WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(confirmUpdateStmt);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[Error] Value not updated");
        }
    }

    /**
     * Delete a row in bookings(table)
     *
     * Parameter variable (id)
     */
    public void deleteValueBooking(int id) {
        try {
            String deleteStmt = "DELETE FROM bookings WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(deleteStmt);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            confirmDelete(id);

        } catch (SQLException e) {
            System.out.println("[Error] Failure to delete a record of booking history");
            e.printStackTrace();
        }
    }

    // confirm deletion
    public void confirmDelete(int id) {
        String confirmDeleteStmt = "Select * FROM bookings WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(confirmDeleteStmt);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Target record successfully deleted!");
        }
    }

    /**
     * Closes the database connection
     * Should be called when the application is shutting down
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[Info] Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("[Error] Error closing database connection");
            e.printStackTrace();
        }
    }

    /**
     * Check if database connection is active
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
