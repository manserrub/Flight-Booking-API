package com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.AirportEntity;
import org.springframework.stereotype.Component;

@Component
public class AirportPersistenceMapper {

    public Airport toDomain(AirportEntity entity) {
        return new Airport(
                entity.getId(),
                entity.getIataCode(),
                entity.getName(),
                entity.getCity(),
                entity.getCountry()
        );
    }

    public AirportEntity toEntity(Airport airport) {
        return new AirportEntity(
                airport.getId(),
                airport.getIataCode(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry()
        );
    }
}
