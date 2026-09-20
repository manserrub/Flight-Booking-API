package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when a passenger is not found.
 */
public class PassengerNotFoundException extends RuntimeException {
    public PassengerNotFoundException(String message) {
        super(message);
    }

    public PassengerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static PassengerNotFoundException byId(Long id) {
        return new PassengerNotFoundException("Passenger with id " + id + " not found");
    }

    public static PassengerNotFoundException byPassportNumber(String passportNumber) {
        return new PassengerNotFoundException("Passenger with passport number '" + passportNumber + "' not found");
    }
}
