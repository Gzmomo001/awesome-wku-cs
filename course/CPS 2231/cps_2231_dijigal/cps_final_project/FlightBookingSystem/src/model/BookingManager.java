package model;

import DatabaseManagement.DatabaseManager;
import java.util.*;

/**
 * BookingManager handles booking operations using SQL database storage.
 * Replaces file-based storage with database operations via DatabaseManager.
 */
public class BookingManager {
    private List<Booking> bookings = new ArrayList<>();
    private DatabaseManager dbManager;

    public BookingManager() {
        // Initialize database connection
        dbManager = new DatabaseManager();
    }

    /**
     * Add a booking and save it to the database
     * 
     * @param booking The booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);

        // Save to database instead of file
        if (dbManager.isConnected()) {
            boolean success = dbManager.insertBooking(booking);
            if (success) {
                System.out.println("✓ Booking saved to database");
            } else {
                System.err.println("✗ Failed to save booking to database");
            }
        } else {
            System.err.println("✗ Database not connected, booking not saved");
        }
    }

    /**
     * Get all bookings (in-memory list)
     * 
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookings;
    }

    /**
     * Read all bookings from database
     * 
     * @return List of booking summaries as strings
     */
    public List<String> getAllBookingsFromDB() {
        if (dbManager.isConnected()) {
            return dbManager.getAllBookings();
        } else {
            System.err.println("✗ Database not connected");
            return new ArrayList<>();
        }
    }

    /**
     * Legacy method for compatibility - now reads from database
     * 
     * @return List of booking strings from database
     */
    public List<String> readBookingsFromFile() {
        return getAllBookingsFromDB();
    }

    /**
     * Close database connection when app shuts down
     */
    public void shutdown() {
        if (dbManager != null) {
            dbManager.closeConnection();
        }
    }
}
