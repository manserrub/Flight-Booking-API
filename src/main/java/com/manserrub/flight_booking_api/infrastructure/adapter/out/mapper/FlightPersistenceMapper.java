package com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.AirportEntity;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.FlightEntity;
import org.springframework.stereotype.Component;

@Component
public class FlightPersistenceMapper {

    private final AirportPersistenceMapper airportMapper;

    public FlightPersistenceMapper(AirportPersistenceMapper airportMapper) {
        this.airportMapper = airportMapper;
    }

    public Flight toDomain(FlightEntity entity) {
        Airport departureAirport = airportMapper.toDomain(entity.getDepartureAirport());
        Airport arrivalAirport = airportMapper.toDomain(entity.getArrivalAirport());

        return new Flight(
                entity.getId(),
                entity.getFlightNumber(),
                departureAirport,
                arrivalAirport,
                entity.getDepartureTime(),
                entity.getArrivalTime(),
                entity.getPrice(),
                entity.getAvailableSeats(),
                Flight.FlightStatus.valueOf(entity.getStatus())
        );
    }

    public FlightEntity toEntity(Flight domain) {
        AirportEntity departureAirport = airportMapper.toEntity(domain.getDepartureAirport());
        AirportEntity arrivalAirport = airportMapper.toEntity(domain.getArrivalAirport());

        return new FlightEntity(
                domain.getId(),
                domain.getFlightNumber(),
                departureAirport,
                arrivalAirport,
                domain.getDepartureTime(),
                domain.getArrivalTime(),
                domain.getPrice(),
                domain.getAvailableSeats(),
                domain.getStatus().name()
        );
    }
}
