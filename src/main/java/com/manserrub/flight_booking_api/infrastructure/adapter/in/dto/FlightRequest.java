package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record FlightRequest(
        @NotBlank(message = "flightNumber is required")
        String flightNumber,

        @NotNull(message = "departureAirportId is required")
        Long departureAirportId,

        @NotNull(message = "arrivalAirportId is required")
        Long arrivalAirportId,

        @NotNull(message = "departureTime is required")
        LocalDateTime departureTime,

        @NotNull(message = "arrivalTime is required")
        LocalDateTime arrivalTime,

        @NotNull(message = "price is required")
        @Positive(message = "price must be greater than 0")
        Double price,

        @NotNull(message = "availableSeats is required")
        Integer availableSeats
) {
}
