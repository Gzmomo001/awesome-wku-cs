package crawler;
//Data model for flight information scraped from Ctrip
public class FlightData implements Comparable<FlightData> {
    private String flightNumber;
    private String airline;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private String origin;
    private String destination;
    private boolean hasTransfer;
    private int transferCount;
    private String date;

    public FlightData() {
    }

    public FlightData(String flightNumber, String airline, String departureTime,
            String arrivalTime, double price, String origin, String destination,
            boolean hasTransfer, int transferCount, String date) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.origin = origin;
        this.destination = destination;
        this.hasTransfer = hasTransfer;
        this.transferCount = transferCount;
        this.date = date;
    }

    // Getters
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasTransfer() {
        return hasTransfer;
    }

    public int getTransferCount() {
        return transferCount;
    }

    public String getDate() {
        return date;
    }

    /**
     * Compare flights: direct flights first, then by price
     */
    @Override
    public int compareTo(FlightData other) {
        // Direct flights have priority over transfer flights
        if (this.hasTransfer != other.hasTransfer) {
            return this.hasTransfer ? 1 : -1;
        }

        // If both have transfers, fewer transfers are better
        if (this.hasTransfer && this.transferCount != other.transferCount) {
            return Integer.compare(this.transferCount, other.transferCount);
        }

        // Compare by price
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        String transferInfo = hasTransfer
                ? String.format(" (%d transfer%s)", transferCount, transferCount > 1 ? "s" : "")
                : " (Direct)";

        return String.format("%s %s: %s → %s | %s - %s | ¥%.2f%s",
                airline, flightNumber, origin, destination,
                departureTime, arrivalTime, price, transferInfo);
    }
}
