package com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportRequest;
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

    public Airport toDomain(AirportRequest request) {
        return new Airport(
                null,
                request.iataCode(),
                request.name(),
                request.city(),
                request.country()
        );
    }

    public Airport toDomain(Long id, AirportRequest request) {
        return new Airport(
                id,
                request.iataCode(),
                request.name(),
                request.city(),
                request.country()
        );
    }

    public Airport toDomainPatch(Long id, com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportPatchRequest request) {
        return new Airport(
                id,
                request.iataCode(),
                request.name(),
                request.city(),
                request.country()
        );
    }
}
