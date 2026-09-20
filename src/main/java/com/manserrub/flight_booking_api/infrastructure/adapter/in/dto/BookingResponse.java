package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        String bookingReference,
        FlightResponse flight,
        PassengerResponse passenger,
        LocalDateTime bookingDate,
        String status,
        String seatNumber
) {
}
