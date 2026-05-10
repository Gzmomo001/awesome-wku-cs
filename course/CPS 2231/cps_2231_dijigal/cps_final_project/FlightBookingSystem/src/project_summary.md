# Project Documentation: Flight Booking System

## 0. Flight Ticket Booking System
### Objective: Manage flights, passengers, and bookings.
### Example OOP Concepts: Classes (Flight, Passenger, Booking), Inheritance (Domestic/InternationalFlight), Polymorphism (display methods), Interfaces (Payment), Encapsulation, Abstraction.
### Scope: Add, cancel, search bookings; basic console and GUI interface.


```
src/
├── app/
│   ├── FlightSearchView.java
│   ├── Main.java
│   └── PassengerInfoView.java
├── crawler/
│   ├── CityCodeMapper.java
│   ├── FlightCrawler.java
│   ├── FlightData.java
│   └── FlightRouteGenerator.java
├── model/
│   ├── Booking.java
│   ├── BookingManager.java
│   ├── DatabaseManager.java
│   ├── DomesticFlight.java
│   ├── Flight.java
│   ├── InternationalFlight.java
│   └── Passenger.java
└── payment/
    └── Payment.java
```


## 1. Package Information
The project is organized into four main packages, adhering to the MVC (Model-View-Controller) pattern:

*   **`app`**: Contains the User Interface (UI) logic built with JavaFX.
    *   `Main.java`: Entry point, manages the application lifecycle and scene navigation.
    *   `FlightSearchView.java`: Handles the flight search interface (Step 1).
    *   `PassengerInfoView.java`: Manages passenger details collection and booking confirmation (Step 2).
*   **`crawler`**: Handles data acquisition from external sources.
    *   `FlightCrawler.java`: Uses Microsoft Playwright to scrape real-time flight data from Ctrip.
    *   `CityCodeMapper.java`: Maps city names to airport codes (e.g., "Shanghai" -> "SHA").
    *   `FlightData.java`: Data model for scraped flight information.
*   **`model`**: Defines the core business logic and data structures.
    *   `Flight.java`, `DomesticFlight.java`, `InternationalFlight.java`: Flight hierarchy.
    *   `Booking.java`: Represents a reservation.
    *   `Passenger.java`: Stores passenger details.
    *   `DatabaseManager.java`: Handles MySQL database interactions (CRUD operations).
    *   `BookingManager.java`: High-level manager for booking operations.
*   **`payment`**: Manages payment processing.
    *   `Payment.java`: Simulates payment processing (Credit Card, PayPal).

## 2. Maven & JavaFX Usage
*   **Maven**: The project uses Apache Maven for build automation and dependency management. Key dependencies likely include:
    *   `com.microsoft.playwright`: For web scraping.
    *   `mysql-connector-j`: For database connectivity.
    *   `org.openjfx`: For the JavaFX UI controls and graphics.
*   **JavaFX**: The user interface is built using JavaFX.
    *   **Scene Graph**: The UI is constructed programmatically (no FXML) using layouts like `VBox`, `HBox`, and controls like `TextField`, `DatePicker`, `TableView`.
    *   **Concurrency**: `javafx.concurrent.Task` is used to run the web crawler in a background thread, preventing the UI from freezing during data fetching.

## 3. System Functionalities
*   **Flight Search**: Users can search for flights by Origin, Destination, and Date. The system auto-detects if a flight is Domestic or International.
*   **Real-time Crawling**: Fetches live flight data (prices, times, airlines) from Ctrip.
*   **Booking Management**:
    *   **Create**: Users can book a selected flight.
    *   **Read**: View booking history and details.
    *   **Update**: Modify booking details (e.g., passenger info).
    *   **Delete**: Cancel bookings.
*   **Passenger Management**: Collects and validates passenger name, passport number, and phone number.
*   **Payment Simulation**: Supports multiple payment methods (Credit Card, PayPal) via a polymorphic interface.
*   **Database Persistence**: All bookings are stored in a local MySQL database.

## 4. Inheritance & Polymorphism
*   **Inheritance**:
    *   `Flight` is the abstract base class containing common attributes (flight number, origin, destination).
    *   `DomesticFlight` extends `Flight` adding `airline`.
    *   `InternationalFlight` extends `Flight` adding `visaStatus`.
*   **Polymorphism**:
    *   The `Booking` class works with the abstract `Flight` type, allowing it to handle both domestic and international flights uniformly.
    *   `Flight.getDetails()` is overridden in subclasses to provide specific descriptions.

## 5. Data Security
*   **Database Connection**: Uses JDBC `PreparedStatement` for most operations, which protects against SQL Injection for value insertion.
*   **Vulnerabilities**:
    *   **Hardcoded Credentials**: Database username and password are stored in plain text in `DatabaseManager.java`. **Risk**: High.
    *   **SQL Injection**: The `updateValueBookings` method concatenates the column name (`key`) directly into the SQL string. **Risk**: Medium (if `key` input is not strictly validated).
    *   **Sensitive Data**: Passport numbers and phone numbers are stored in plain text. **Risk**: High (should be encrypted).

## 6. Flight Crawling: Origin & Future
*   **Origin**: The crawler targets **Ctrip (Trip.com)**, a major travel aggregator. It uses **Microsoft Playwright** to render the JavaScript-heavy flight list pages and extract data from the DOM.
*   **Future Implementation**:
    *   **Anti-Bot Evasion**: Implement proxy rotation and user-agent randomization to avoid IP bans.
    *   **Multi-Source**: Expand to crawl other sites (Expedia, Skyscanner) for price comparison.
    *   **Headless Mode**: Fully optimize for headless execution to run faster on servers.
    *   **API Integration**: Replace scraping with official airline APIs (NDC) for more reliability if access is granted.

## 7. Code Complexity & Weaknesses
*   **Complexity**:
    *   **Moderate**. The project successfully integrates three distinct technologies: JavaFX (UI), Playwright (Scraping), and JDBC (SQL).
    *   **Concurrency**: Handling the crawler in a separate thread adds complexity but is handled correctly with JavaFX `Task`.
*   **Weaknesses**:
    *   **Hardcoded Configuration**: Database credentials and file paths (e.g., CSV path) are hardcoded.
    *   **Error Handling**: While basic try-catch blocks exist, network timeouts or crawler failures could be handled more gracefully with automatic retries.
    *   **Security**: As noted, credentials and sensitive user data are not secured.
    *   **Scalability**: The current crawler is synchronous per URL (though threaded in `main` crawler test, the UI triggers a single search). Bulk searching would require a job queue system.
