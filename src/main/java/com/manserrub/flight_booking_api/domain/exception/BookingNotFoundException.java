package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when a booking is not found.
 */
public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BookingNotFoundException byId(Long id) {
        return new BookingNotFoundException("Booking with id " + id + " not found");
    }

    public static BookingNotFoundException byReference(String reference) {
        return new BookingNotFoundException("Booking with reference '" + reference + "' not found");
    }
}
