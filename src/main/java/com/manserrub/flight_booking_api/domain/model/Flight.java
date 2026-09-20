package com.manserrub.flight_booking_api.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a flight domain model.
 * This is a domain entity with no Spring framework dependencies.
 */
public class Flight {

    /**
     * Enum for flight status.
     */
    public enum FlightStatus {
        SCHEDULED, BOARDING, DEPARTED, LANDED, CANCELLED
    }

    private Long id;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
    private Integer availableSeats;
    private FlightStatus status;

    /**
     * Default constructor required for framework instantiation.
     */
    public Flight() {
    }

    /**
     * Constructor with all fields.
     */
    public Flight(Long id, String flightNumber, Airport departureAirport, Airport arrivalAirport,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, Double price,
                  Integer availableSeats, FlightStatus status) {
        this.id = id;
        this.flightNumber = validateFlightNumber(flightNumber);
        this.departureAirport = Objects.requireNonNull(departureAirport, "Departure airport cannot be null");
        this.arrivalAirport = Objects.requireNonNull(arrivalAirport, "Arrival airport cannot be null");
        this.departureTime = Objects.requireNonNull(departureTime, "Departure time cannot be null");
        this.arrivalTime = Objects.requireNonNull(arrivalTime, "Arrival time cannot be null");
        validateTimes(departureTime, arrivalTime);
        this.price = validatePrice(price);
        this.availableSeats = validateAvailableSeats(availableSeats);
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Constructor without ID (for new flights).
     */
    public Flight(String flightNumber, Airport departureAirport, Airport arrivalAirport,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, Double price,
                  Integer availableSeats, FlightStatus status) {
        this(null, flightNumber, departureAirport, arrivalAirport, departureTime, arrivalTime,
                price, availableSeats, status);
    }

    /**
     * Validates flight number format (e.g., "AA123", "BA001").
     */
    private static String validateFlightNumber(String flightNumber) {
        Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        if (!flightNumber.matches("[A-Z]{2}\\d{3,4}")) {
            throw new IllegalArgumentException("Flight number must be in format: 2 uppercase letters + 3-4 digits");
        }
        return flightNumber;
    }

    /**
     * Validates that departure time is before arrival time.
     */
    private static void validateTimes(LocalDateTime departure, LocalDateTime arrival) {
        if (departure.isAfter(arrival) || departure.equals(arrival)) {
            throw new IllegalArgumentException("Departure time must be before arrival time");
        }
    }

    /**
     * Validates price is positive.
     */
    private static Double validatePrice(Double price) {
        Objects.requireNonNull(price, "Price cannot be null");
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        return price;
    }

    /**
     * Validates available seats is non-negative.
     */
    private static Integer validateAvailableSeats(Integer seats) {
        Objects.requireNonNull(seats, "Available seats cannot be null");
        if (seats < 0) {
            throw new IllegalArgumentException("Available seats cannot be negative");
        }
        return seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = validateFlightNumber(flightNumber);
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = Objects.requireNonNull(departureAirport, "Departure airport cannot be null");
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = Objects.requireNonNull(arrivalAirport, "Arrival airport cannot be null");
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = Objects.requireNonNull(departureTime, "Departure time cannot be null");
        validateTimes(this.departureTime, this.arrivalTime);
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = Objects.requireNonNull(arrivalTime, "Arrival time cannot be null");
        validateTimes(this.departureTime, this.arrivalTime);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = validatePrice(price);
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = validateAvailableSeats(availableSeats);
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    /**
     * Checks if a seat is available on this flight.
     */
    public boolean hasSeatAvailable() {
        return availableSeats > 0;
    }

    /**
     * Reserves a seat if available.
     */
    public void reserveSeat() {
        if (!hasSeatAvailable()) {
            throw new IllegalArgumentException("No seats available on this flight");
        }
        availableSeats--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id) &&
                Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flightNumber);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", departureAirport=" + departureAirport.getIataCode() +
                ", arrivalAirport=" + arrivalAirport.getIataCode() +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                ", status=" + status +
                '}';
    }
}
