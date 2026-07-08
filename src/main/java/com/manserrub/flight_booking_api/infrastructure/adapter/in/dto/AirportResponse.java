package com.manserrub.flight_booking_api.infrastructure.adapter.in.dto;

public record AirportResponse(Long id, String iataCode, String name, String city, String country) {
}
