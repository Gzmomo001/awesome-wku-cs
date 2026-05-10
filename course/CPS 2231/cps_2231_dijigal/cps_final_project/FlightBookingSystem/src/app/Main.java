package app;

import java.lang.Thread;
import javafx.application.Application;
import javafx.stage.Stage;
import model.BookingManager;

/**
 * Main Application Entry Point
 * Implements a multi-step booking wizard:
 * - Step 1: Flight search (FlightSearchView)
 * - Step 2: Passenger information and booking (PassengerInfoView)
 */
public class Main extends Application {
    private BookingManager bookingManager;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.bookingManager = new BookingManager();

        primaryStage.setTitle("✈️ Flight Booking System");

        // Show Step 1: Flight Search
        showFlightSearchView();

        primaryStage.show();
    }

    /**
     * Display the flight search view (Step 1)
     */
    private void showFlightSearchView() {
        FlightSearchView searchView = new FlightSearchView(primaryStage);
        primaryStage.setScene(searchView.buildScene());

        // When user clicks "Search Flights", go to passenger info view
        searchView.getSearchButton().setOnAction(e -> {
            FlightSearchView.SearchData searchData = searchView.getSearchData();
            if (searchData != null) { // Validation passed
                performFlightSearch(searchData);
            }
        });
    }

    /**
     * Perform flight search using the crawler in a background thread
     */
    private void performFlightSearch(FlightSearchView.SearchData searchData) {
        // Show loading state
        javafx.scene.control.Alert loadingAlert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        loadingAlert.setTitle("Searching Flights");
        loadingAlert.setHeaderText("Please wait...");
        loadingAlert.setContentText(
                "Crawling flight data from Ctrip for " + searchData.origin + " to " + searchData.destination + "...");
        loadingAlert.show();

        // Run crawler in background task
        javafx.concurrent.Task<java.util.List<crawler.FlightCrawler.FlightInfo>> task = new javafx.concurrent.Task<>() {
            @Override
            protected java.util.List<crawler.FlightCrawler.FlightInfo> call() throws Exception {
                return crawler.FlightCrawler.searchFlights(searchData.origin, searchData.destination, searchData.date);
            }
        };

        task.setOnSucceeded(event -> {
            loadingAlert.close();
            java.util.List<crawler.FlightCrawler.FlightInfo> results = task.getValue();

            if (results.isEmpty()) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("No Flights Found");
                alert.setHeaderText(null);
                alert.setContentText(
                        "No flights found for the selected route and date. Please try different parameters.");
                alert.showAndWait();
            } else {
                // Show selection dialog
                showFlightSelectionDialog(results, searchData);
            }
        });

        task.setOnFailed(event -> {
            loadingAlert.close();
            Throwable ex = task.getException();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Search Error");
            alert.setContentText("An error occurred while searching: " + ex.getMessage());
            alert.showAndWait();
            ex.printStackTrace();
        });

        new Thread(task).start();
    }

    /**
     * Show dialog to select a flight from results
     */
    private void showFlightSelectionDialog(java.util.List<crawler.FlightCrawler.FlightInfo> flights,
            FlightSearchView.SearchData searchData) {
        javafx.scene.control.ChoiceDialog<crawler.FlightCrawler.FlightInfo> dialog = new javafx.scene.control.ChoiceDialog<>(
                flights.get(0), flights);
        dialog.setTitle("Select Flight");
        dialog.setHeaderText("Found " + flights.size() + " flights");
        dialog.setContentText("Choose a flight:");

        // Custom list cell to show details nicely
        dialog.getDialogPane().setPrefWidth(600);

        java.util.Optional<crawler.FlightCrawler.FlightInfo> result = dialog.showAndWait();
        result.ifPresent(flight -> showPassengerInfoView(searchData, flight));
    }

    /**
     * Display the passenger information view (Step 2)
     */
    /**
     * Display the passenger information view (Step 2)
     */
    private void showPassengerInfoView(FlightSearchView.SearchData searchData,
            crawler.FlightCrawler.FlightInfo selectedFlight) {
        PassengerInfoView passengerView = new PassengerInfoView(primaryStage, searchData, bookingManager,
                selectedFlight);
        primaryStage.setScene(passengerView.buildScene());

        // Back button returns to flight search
        passengerView.getBackButton().setOnAction(e -> {
            showFlightSearchView();
        });

        // Book button processes the booking
        passengerView.getBookButton().setOnAction(e -> {
            passengerView.processBooking();
        });
    }

    // Display detailed flight info.
    /*
     * DATABASE.fetch(
     * "SELECT * FROM flights WHERE origin = ?  destination = ?  departure_date = ? , price = ?"
     * )
     * 
     */

    @Override
    public void stop() {
        // Clean up database connection when application closes
        System.out.println("\n=== Application Closing ===");
        if (bookingManager != null) {
            bookingManager.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
