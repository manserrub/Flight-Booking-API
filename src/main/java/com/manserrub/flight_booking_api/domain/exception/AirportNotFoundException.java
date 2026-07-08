package com.manserrub.flight_booking_api.domain.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String message) {
        super(message);
    }
}
