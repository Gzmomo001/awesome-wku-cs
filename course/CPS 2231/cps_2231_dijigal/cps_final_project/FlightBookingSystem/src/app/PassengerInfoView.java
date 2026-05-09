package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.*;
import DatabaseManagement.*;
import payment.*;
import crawler.*;

import java.util.List;

/**
 * PassengerInfoView - Step 2 of the booking wizard
 * Displays flight search summary and collects passenger information
 */
public class PassengerInfoView {
    private Stage stage;
    private FlightSearchView.SearchData searchData;
    private BookingManager bookingManager;
    private FlightCrawler.FlightInfo selectedFlight;

    // UI Components
    private TextField nameField;
    private TextField passportField;
    private TextField phoneNumber;
    private Button backButton;
    private Button modifyButton;
    private Button bookButton;
    private TextArea urlDisplayArea;

    // Additional fields for booking
    private String paymentMethod = "Credit Card"; // Default

    public PassengerInfoView(Stage stage, FlightSearchView.SearchData searchData, BookingManager bookingManager,
            FlightCrawler.FlightInfo selectedFlight) {
        this.stage = stage;
        this.searchData = searchData;
        this.bookingManager = bookingManager;
        this.selectedFlight = selectedFlight;
    }

    /**
     * Build and return the scene for passenger information
     */
    public Scene buildScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("✈️ Passenger Information");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));

        // Flight search summary box
        VBox summaryBox = buildSummaryBox();

        // Passenger info form
        VBox formBox = buildPassengerForm();

        // URL display area
        VBox urlBox = buildURLDisplayBox();

        // Button panel
        HBox buttonPanel = buildButtonPanel();

        root.getChildren().addAll(title, summaryBox, formBox, buttonPanel, urlBox);

        return new Scene(root, 700, 700);
    }

    private VBox buildSummaryBox() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(20));
        box.setMaxWidth(600);
        box.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        Label summaryTitle = new Label("Your Selected Flight:");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        summaryTitle.setTextFill(Color.WHITE);

        String summaryText = String.format( // format string into a single line:
                "%s → %s, Date: %s, Airline: %s, ( %s), Time: %s - %s, Price: ¥%s",
                CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin)),
                // CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin))
                searchData.destination,
                searchData.date,
                selectedFlight != null ? selectedFlight.getAirline() : "?",
                selectedFlight != null ? selectedFlight.getFlightNumber() : "？",
                selectedFlight != null ? selectedFlight.getDepartureTime() : "00:00",
                selectedFlight != null ? selectedFlight.getArrivalTime() : "00:00",
                selectedFlight != null ? selectedFlight.getPrice() : "0");

        Label summaryLabel = new Label(summaryText);
        summaryLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 10));
        summaryLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(summaryTitle, summaryLabel);
        return box;
    }

    private VBox buildPassengerForm() {
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(25));
        formBox.setMaxWidth(650);
        formBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Passenger phone number
        Label phoneNumberLabel = new Label("Phone Number:");
        phoneNumberLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        phoneNumber = new TextField();
        phoneNumber.setPromptText("e.g., +65 12345678");
        phoneNumber.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Passenger name
        Label nameLabel = new Label("Passenger Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        nameField = new TextField();
        nameField.setPromptText("Full name as on passport");
        nameField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Passport number
        Label passportLabel = new Label("Passport Number:");
        passportLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        passportField = new TextField();
        passportField.setPromptText("e.g., AB1234567");
        passportField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        formBox.getChildren().addAll(
                nameLabel, nameField,
                passportLabel, passportField,
                phoneNumberLabel, phoneNumber);

        return formBox;
    }

    private VBox buildURLDisplayBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setMinHeight(400);
        box.setMaxWidth(600);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label urlTitle = new Label(" Airline Booking Links (From Price Comparison)");
        urlTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        urlTitle.setTextFill(Color.web("#2c3e50"));

        urlDisplayArea = new TextArea();
        urlDisplayArea.setEditable(false);
        urlDisplayArea.setPrefRowCount(8);
        urlDisplayArea.setWrapText(true);
        urlDisplayArea.setStyle(
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                        "-fx-font-size: 11px;" +
                        "-fx-control-inner-background: #f8f9fa;");
        urlDisplayArea.setText("Booking URLs will appear here after you complete the booking...");

        box.getChildren().addAll(urlTitle, urlDisplayArea);
        box.setVisible(false); // Hidden initially
        return box;
    }

    /**
     * Open a modify booking window that allows users to show or delete bookings by
     * phone number
     */
    private void openModifyWindow() {
        Stage modifyStage = new Stage();
        modifyStage.setTitle("Modify Booking");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("📝 Modify Booking");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        // Phone number input section
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(20));
        inputBox.setMaxWidth(500);
        inputBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter phone number to search...");
        phoneField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Button panel
        HBox buttonPanel = new HBox(15);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setPadding(new Insets(10, 0, 0, 0));

        Button showButton = new Button("Show");
        showButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        // Hover effects
        showButton.setOnMouseEntered(e -> showButton.setStyle(
                "-fx-background-color: #2980b9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        showButton.setOnMouseExited(e -> showButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand."));

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle(
                "-fx-background-color: #c0392b;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        buttonPanel.getChildren().addAll(showButton, deleteButton);
        inputBox.getChildren().addAll(phoneLabel, phoneField, buttonPanel);

        // Display area for results
        VBox displayBox = new VBox(8);
        displayBox.setPadding(new Insets(15));
        displayBox.setMaxWidth(500);
        displayBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label displayTitle = new Label("📋 Booking Information:");
        displayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        displayTitle.setTextFill(Color.web("#2c3e50"));

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setPrefRowCount(10);
        displayArea.setWrapText(true);
        displayArea.setStyle(
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                        "-fx-font-size: 12px;" +
                        "-fx-control-inner-background: #f8f9fa;");
        displayArea.setText("Booking information will appear here...");

        displayBox.getChildren().addAll(displayTitle, displayArea);

        // Show button action
        showButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                displayArea.setText("Error: Please enter a phone number.");
                return;
            }

            try {
                DatabaseManager dbManager = new DatabaseManager();
                List<String> results = dbManager.getPersonalBooking(phone);

                if (results == null || results.isEmpty()) {
                    displayArea.setText("No bookings found for phone number: " + phone);
                    // Update main URL display area
                    urlDisplayArea.setText("No bookings found for phone number: " + phone);
                    urlDisplayArea.getParent().setVisible(true);
                } else {
                    // Combine all booking results
                    StringBuilder resultText = new StringBuilder("Booking Details:\n\n");
                    for (String result : results) {
                        resultText.append(result).append("\n---\n\n");
                    }
                    displayArea.setText(resultText.toString());
                    // Update main URL display area with booking info
                    urlDisplayArea.setText("=== Booking Information for " + phone + " ===\n\n" + resultText.toString());
                    urlDisplayArea.getParent().setVisible(true);
                }
            } catch (Exception ex) {
                displayArea.setText("Error retrieving booking: " + ex.getMessage());
                urlDisplayArea.setText("Error: " + ex.getMessage());
                urlDisplayArea.getParent().setVisible(true);
                ex.printStackTrace();
            }
        });

        // Delete button action
        deleteButton.setOnAction(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                displayArea.setText("Error: Please enter a phone number.");
                return;
            }

            // Show confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Booking");
            confirmAlert.setContentText("Are you sure you want to delete the booking for phone: " + phone + "?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        DatabaseManager dbManager = new DatabaseManager();
                        // First get the booking to find the ID
                        List<String> bookings = dbManager.getPersonalBooking(phone);

                        if (bookings == null || bookings.isEmpty()) {
                            displayArea.setText("No bookings found for phone number: " + phone);
                            urlDisplayArea.setText("Delete Failed: No bookings found for phone number: " + phone);
                            urlDisplayArea.getParent().setVisible(true);
                            return;
                        }

                        // Extract ID from booking info (assuming booking string contains ID)
                        // Since getPersonalBooking doesn't return ID, we need to use getAllBookings and
                        // find it
                        List<String> allBookings = dbManager.getAllBookings();
                        int bookingId = extractBookingIdByPhone(allBookings, phone);

                        if (bookingId == -1) {
                            displayArea.setText("Error: Could not find booking ID for phone: " + phone);
                            urlDisplayArea.setText("Delete Failed: Could not find booking ID");
                            urlDisplayArea.getParent().setVisible(true);
                            return;
                        }

                        // Delete the booking
                        dbManager.deleteValueBooking(bookingId);
                        displayArea.setText(
                                "✓ Booking deleted successfully!\nBooking ID: " + bookingId + "\nPhone: " + phone);

                        // Update main URL display area
                        urlDisplayArea.setText("=== Booking Deleted ===\n\nBooking ID: " + bookingId
                                + "\nPhone Number: " + phone + "\nStatus: Successfully deleted from database");
                        urlDisplayArea.getParent().setVisible(true);

                        // Clear the phone field
                        phoneField.clear();
                    } catch (Exception ex) {
                        displayArea.setText("Error deleting booking: " + ex.getMessage());
                        urlDisplayArea.setText("Delete Error: " + ex.getMessage());
                        urlDisplayArea.getParent().setVisible(true);
                        ex.printStackTrace();
                    }
                }
            });
        });

        root.getChildren().addAll(title, inputBox, displayBox);

        Scene modifyScene = new Scene(root, 600, 550);
        modifyStage.setScene(modifyScene);
        modifyStage.show();
    }

    /**
     * Extract booking ID from all bookings list by matching phone number
     * 
     * @param allBookings List containing all booking information strings
     * @param phone       Phone number to search for
     * @return Booking ID if found, -1 otherwise
     */
    private int extractBookingIdByPhone(List<String> allBookings, String phone) {
        try {
            // Combine all booking strings into one
            StringBuilder combined = new StringBuilder();
            for (String booking : allBookings) {
                combined.append(booking).append("\n");
            }
            String allBookingsStr = combined.toString();

            String[] lines = allBookingsStr.split("\n");
            for (String line : lines) {
                if (line.contains("Phone:") && line.contains(phone)) {
                    // Search backwards for the ID line
                    int currentIndex = java.util.Arrays.asList(lines).indexOf(line);
                    for (int i = currentIndex; i >= 0; i--) {
                        if (lines[i].contains("[#") && lines[i].contains("]")) {
                            // Extract ID from "[#123] Name (Passport: ..., Phone: ...)"
                            String idStr = lines[i].substring(lines[i].indexOf("[#") + 2, lines[i].indexOf("]"));
                            return Integer.parseInt(idStr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting booking ID: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    private HBox buildButtonPanel() {
        HBox panel = new HBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(650);
        panel.setPadding(new Insets(10, 0, 10, 0));

        backButton = new Button("← Back to Search");
        backButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        modifyButton = new Button("🔧 Modify");
        modifyButton.setStyle(
                "-fx-background-color: #f39c12;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        bookButton = new Button("Book Flight & show Links");
        bookButton.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        // Hover effects
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #7f8c8d;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        modifyButton.setOnMouseEntered(e -> modifyButton.setStyle(
                "-fx-background-color: #d68910;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        modifyButton.setOnMouseExited(e -> modifyButton.setStyle(
                "-fx-background-color: #f39c12;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        bookButton.setOnMouseEntered(e -> bookButton.setStyle(
                "-fx-background-color: #229954;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        bookButton.setOnMouseExited(e -> bookButton.setStyle(
                "-fx-background-color: #c8e0d2ff;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        // Modify button action
        modifyButton.setOnAction(e -> openModifyWindow());

        panel.getChildren().addAll(backButton, modifyButton, bookButton);
        return panel;
    }

    /**
     * Process the booking and display airline URLs
     */
    public void processBooking() {
        String name = nameField.getText().trim();
        String passport = passportField.getText().trim();
        String phoneNumber = this.phoneNumber.getText().trim();

        // Validate inputs
        if (name.isEmpty() || passport.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Missing Information", "Please fill in all passenger fields.");
            return;
        }

        try {
            // Create booking objects
            Passenger passenger = new Passenger(name, passport, phoneNumber);

            // Use real data from selectedFlight
            String flightNum = selectedFlight != null ? selectedFlight.getFlightNumber() : "N/A";
            String airline = selectedFlight != null ? selectedFlight.getAirline() : "N/A";
            // Sanitize and ensure time format is HH:MM:SS for SQL Time
            // Remove any Chinese day indicators (+1天, +2天, etc.) from times
            String rawDepTime = selectedFlight != null ? selectedFlight.getDepartureTime() : "00:00";
            String rawArrTime = selectedFlight != null ? selectedFlight.getArrivalTime() : "00:00";
            String depTime = sanitizeTime(rawDepTime);
            String arrTime = sanitizeTime(rawArrTime);

            // Clean price string (remove currency symbol if present)
            double priceVal = 0.0;
            if (selectedFlight != null) {
                try {
                    String priceStr = selectedFlight.getPrice().replaceAll("[^0-9.]", "");
                    priceVal = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price: " + selectedFlight.getPrice());
                }
            }

            Flight flight;
            if (searchData.flightType.equals("Domestic")) {
                flight = new DomesticFlight(flightNum,
                        CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin)),
                        searchData.destination,
                        searchData.date, depTime, arrTime, airline);
            } else {
                flight = new InternationalFlight(flightNum,
                        CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin)),
                        searchData.destination,
                        searchData.date, depTime, arrTime, "Required"); // Visa status
            }

            Payment payment = new Payment();
            if (paymentMethod.equals("Credit Card")) {
                payment.CreditCardPayment(priceVal);
            } else {
                payment.PayPalPayment(priceVal);
            }

            Booking booking = new Booking(flight, passenger, payment);
            booking.confirm(priceVal);
            bookingManager.addBooking(booking);

            // Generate airline URLs
            // CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin))
            List<String> urls = FlightRouteGenerator.generateUrls(
                    CityCodeMapper.getCityName(CityCodeMapper.getCityCode(searchData.origin)),
                    searchData.destination,
                    searchData.date);

            // Display URLs
            StringBuilder urlText = new StringBuilder();
            for (String url : urls) {
                urlText.append(url).append("\n");
            }
            urlDisplayArea.setText(urlText.toString());
            urlDisplayArea.getParent().setVisible(true);

            // Show success message
            showSuccessAlert("Booking Confirmed!",
                    "Your flight has been booked successfully!\n\n" +
                            booking.getSummary() + "\n\n" +
                            "Check the airline links below to compare prices.");

        } catch (Exception e) {
            showAlert("Booking Error", "Failed to process booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getBookButton() {
        return bookButton;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sanitize time strings by removing Chinese day indicators (+1天, +2天, etc.)
     * and ensuring proper HH:MM:SS format for SQL Time
     *
     * @param rawTime Raw time string from crawler (e.g., "14:30", "23:40+2天")
     * @return Properly formatted SQL time string (HH:MM:SS)
     */
    private String sanitizeTime(String rawTime) {
        if (rawTime == null || rawTime.trim().isEmpty()) {
            return "00:00:00";
        }

        // Remove any Chinese day indicators (+1天, +2天, +3天, etc.)
        // Also remove newlines and extra whitespace
        String cleanTime = rawTime.replaceAll("\\s*\\+\\d+天", "").trim();

        // If the time doesn't contain seconds, add ":00"
        if (cleanTime.matches("\\d{1,2}:\\d{2}")) {
            cleanTime += ":00";
        }
        // If the format is still invalid, return default time
        else if (!cleanTime.matches("\\d{1,2}:\\d{2}:\\d{2}")) {
            System.err.println("Warning: Invalid time format '" + rawTime + "', using default 00:00:00");
            return "00:00:00";
        }

        return cleanTime;
    }
}
