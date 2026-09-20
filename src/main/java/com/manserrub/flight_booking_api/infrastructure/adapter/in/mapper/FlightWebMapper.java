package com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper;

import com.manserrub.flight_booking_api.application.service.AirportService;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.FlightRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.FlightResponse;
import org.springframework.stereotype.Component;

@Component
public class FlightWebMapper {

    private final AirportWebMapper airportWebMapper;
    private final AirportService airportService;

    public FlightWebMapper(AirportWebMapper airportWebMapper, AirportService airportService) {
        this.airportWebMapper = airportWebMapper;
        this.airportService = airportService;
    }

    public FlightResponse toResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                airportWebMapper.toResponse(flight.getDepartureAirport()),
                airportWebMapper.toResponse(flight.getArrivalAirport()),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice(),
                flight.getAvailableSeats(),
                flight.getStatus().name()
        );
    }

    public Flight toDomain(FlightRequest request) {
        Airport departureAirport = airportService.findById(request.departureAirportId());
        Airport arrivalAirport = airportService.findById(request.arrivalAirportId());

        return new Flight(
                null,
                request.flightNumber(),
                departureAirport,
                arrivalAirport,
                request.departureTime(),
                request.arrivalTime(),
                request.price(),
                request.availableSeats(),
                Flight.FlightStatus.SCHEDULED
        );
    }

    public Flight toDomain(Long id, FlightRequest request) {
        Airport departureAirport = airportService.findById(request.departureAirportId());
        Airport arrivalAirport = airportService.findById(request.arrivalAirportId());

        return new Flight(
                id,
                request.flightNumber(),
                departureAirport,
                arrivalAirport,
                request.departureTime(),
                request.arrivalTime(),
                request.price(),
                request.availableSeats(),
                Flight.FlightStatus.SCHEDULED
        );
    }
}
