package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

import java.time.LocalDate;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String passportNumber,
        LocalDate dateOfBirth,
        int age
) {
}
