package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PassengerRequest(
        @NotBlank(message = "firstName is required")
        String firstName,

        @NotBlank(message = "lastName is required")
        String lastName,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "phoneNumber is required")
        String phoneNumber,

        @NotBlank(message = "passportNumber is required")
        String passportNumber,

        @NotNull(message = "dateOfBirth is required")
        LocalDate dateOfBirth
) {
}
