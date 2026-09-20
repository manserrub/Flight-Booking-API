package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when a flight is not found.
 */
public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static FlightNotFoundException byId(Long id) {
        return new FlightNotFoundException("Flight with id " + id + " not found");
    }

    public static FlightNotFoundException byFlightNumber(String flightNumber) {
        return new FlightNotFoundException("Flight with number '" + flightNumber + "' not found");
    }
}
