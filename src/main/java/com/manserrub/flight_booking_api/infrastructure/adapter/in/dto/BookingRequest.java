package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "flightId is required")
        Long flightId,

        @NotNull(message = "passengerId is required")
        Long passengerId,

        @NotBlank(message = "seatNumber is required")
        String seatNumber
) {
}
