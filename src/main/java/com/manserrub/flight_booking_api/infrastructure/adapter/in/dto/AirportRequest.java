package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record AirportRequest(
		@NotBlank(message = "iataCode is required")
		@Pattern(regexp = "[A-Z]{3}", message = "iataCode must be exactly 3 uppercase letters")
				String iataCode,
		@NotBlank(message = "name is required")
				String name,
		@NotBlank(message = "city is required")
				String city,
		@NotBlank(message = "country is required")
				String country
) {
}

