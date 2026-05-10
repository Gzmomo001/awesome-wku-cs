package model;

public class DomesticFlight extends Flight {
    private String airline;

    public DomesticFlight(String flightNumber, String origin, String destination, String date, String departureTime,
            String arrivalTime, String airline) {
        super(flightNumber, origin, destination, date, departureTime, arrivalTime);
        this.airline = airline;
    }

    @Override
    public String getDetails() {
        return "Domestic Flight " + flightNumber + " (" + airline + ") from " + origin + " to " + destination + " on "
                + date;
    }

    public String getAirline() {
        return airline;
    }
}
