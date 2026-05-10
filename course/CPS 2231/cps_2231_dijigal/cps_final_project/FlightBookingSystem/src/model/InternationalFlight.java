package model;

public class InternationalFlight extends Flight {
    private String visaStatus;

    public InternationalFlight(String flightNumber, String origin, String destination, String date,
            String departureTime, String arrivalTime, String visaStatus) {
        super(flightNumber, origin, destination, date, departureTime, arrivalTime);
        this.visaStatus = visaStatus;
    }

    @Override
    public String getDetails() {
        return "International Flight " + flightNumber + " from " + origin + " to " + destination + " on " + date
                + " [Visa: " + visaStatus + "]";
    }

    public String getVisaStatus() {
        return visaStatus;
    }
}
