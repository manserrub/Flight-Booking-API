package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when an airport is not found.
 */
public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String message) {
        super(message);
    }

    public AirportNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AirportNotFoundException byId(Long id) {
        return new AirportNotFoundException("Airport with id " + id + " not found");
    }

    public static AirportNotFoundException byIataCode(String iataCode) {
        return new AirportNotFoundException("Airport with IATA code '" + iataCode + "' not found");
    }
}
