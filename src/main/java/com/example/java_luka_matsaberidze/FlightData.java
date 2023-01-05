package com.example.java_luka_matsaberidze;

import java.time.LocalDate;
import java.util.Date;

public class FlightData {
    private String source;
    private String destination;

    private int placesCount;
    private LocalDate flightDate;

    private double price;

    public FlightData(String source, String destination, int placesCount, LocalDate flightDate, double price) {
        this.source = source;
        this.destination = destination;
        this.placesCount = placesCount;
        this.flightDate = flightDate;
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "FlightData{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", placesCount=" + placesCount +
                ", flightDate=" + flightDate +
                ", price=" + price +
                '}';
    }
}
