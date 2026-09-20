package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when flight data is invalid.
 */
public class InvalidFlightDataException extends RuntimeException {
    public InvalidFlightDataException(String message) {
        super(message);
    }

    public InvalidFlightDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidFlightDataException invalidFlightNumber(String flightNumber) {
        return new InvalidFlightDataException("Invalid flight number format: " + flightNumber);
    }

    public static InvalidFlightDataException invalidTimes() {
        return new InvalidFlightDataException("Departure time must be before arrival time");
    }

    public static InvalidFlightDataException invalidPrice(Double price) {
        return new InvalidFlightDataException("Invalid price: " + price);
    }

    public static InvalidFlightDataException invalidSeats(Integer seats) {
        return new InvalidFlightDataException("Invalid number of seats: " + seats);
    }
}
