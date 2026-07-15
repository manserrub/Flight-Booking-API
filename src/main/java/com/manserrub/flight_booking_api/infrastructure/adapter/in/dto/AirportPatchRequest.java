package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.Pattern;

public record AirportPatchRequest(
        @Pattern(regexp = "[A-Z]{3}", message = "iataCode must be exactly 3 uppercase letters")
                String iataCode,
        String name,
        String city,
        String country
) {
}

