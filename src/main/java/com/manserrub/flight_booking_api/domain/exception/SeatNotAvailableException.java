package com.manserrub.flight_booking_api.domain.exception;

/**
 * Exception thrown when attempting to book a seat that is not available.
 */
public class SeatNotAvailableException extends RuntimeException {
    public SeatNotAvailableException(String message) {
        super(message);
    }

    public SeatNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SeatNotAvailableException noSeatsAvailable(String flightNumber) {
        return new SeatNotAvailableException("No seats available on flight " + flightNumber);
    }

    public static SeatNotAvailableException seatAlreadyBooked(String seatNumber) {
        return new SeatNotAvailableException("Seat " + seatNumber + " is already booked");
    }
}
