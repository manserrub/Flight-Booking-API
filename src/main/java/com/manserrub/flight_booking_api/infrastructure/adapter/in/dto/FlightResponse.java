package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String flightNumber,
        AirportResponse departureAirport,
        AirportResponse arrivalAirport,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Double price,
        Integer availableSeats,
        String status
) {
}
