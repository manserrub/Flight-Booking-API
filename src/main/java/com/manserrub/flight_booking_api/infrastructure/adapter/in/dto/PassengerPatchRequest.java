package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.Email;

public record PassengerPatchRequest(
        @Email(message = "email must be valid")
        String email,

        String phoneNumber
) {
}
