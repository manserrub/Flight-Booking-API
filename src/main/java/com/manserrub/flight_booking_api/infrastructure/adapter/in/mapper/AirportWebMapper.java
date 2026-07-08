package com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportResponse;
import org.springframework.stereotype.Component;

@Component
public class AirportWebMapper {
    public AirportResponse toResponse(Airport airport) {
        return new AirportResponse(
            airport.getId(),
            airport.getIataCode(),
            airport.getName(),
            airport.getCity(),
            airport.getCountry()
        );
    }
}
