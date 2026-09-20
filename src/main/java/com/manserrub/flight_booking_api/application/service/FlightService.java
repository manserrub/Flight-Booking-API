package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.FlightNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.ports.in.FindFlightByIdUseCase;
import com.manserrub.flight_booking_api.domain.ports.in.ListFlightsUseCase;
import com.manserrub.flight_booking_api.domain.ports.in.SearchFlightsUseCase;
import com.manserrub.flight_booking_api.domain.ports.out.FlightRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService implements FindFlightByIdUseCase, ListFlightsUseCase, SearchFlightsUseCase {

    private final FlightRepositoryPort repositoryPort;

    public FlightService(FlightRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Flight execute(Long id) {
        return repositoryPort.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
    }

    @Override
    public List<Flight> execute() {
        return repositoryPort.findAll();
    }

    @Override
    public List<Flight> findByDepartureAirportId(Long airportId) {
        return repositoryPort.findByDepartureAirportId(airportId);
    }

    @Override
    public List<Flight> findByArrivalAirportId(Long airportId) {
        return repositoryPort.findByArrivalAirportId(airportId);
    }

    @Override
    public List<Flight> findByRoute(Long departureAirportId, Long arrivalAirportId) {
        return repositoryPort.findByRoute(departureAirportId, arrivalAirportId);
    }

    @Override
    public List<Flight> findByStatus(String status) {
        return repositoryPort.findByStatus(status);
    }

    @Override
    public List<Flight> findAvailableFlights(Long departureAirportId, Long arrivalAirportId, LocalDateTime departureDate) {
        return repositoryPort.findAvailableFlights(departureAirportId, arrivalAirportId, departureDate);
    }

    public Flight create(Flight flight) {
        return repositoryPort.save(flight);
    }

    public Flight update(Long id, Flight flight) {
        Flight existing = repositoryPort.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        Flight toSave = new Flight(
                existing.getId(),
                flight.getFlightNumber() != null ? flight.getFlightNumber() : existing.getFlightNumber(),
                flight.getDepartureAirport() != null ? flight.getDepartureAirport() : existing.getDepartureAirport(),
                flight.getArrivalAirport() != null ? flight.getArrivalAirport() : existing.getArrivalAirport(),
                flight.getDepartureTime() != null ? flight.getDepartureTime() : existing.getDepartureTime(),
                flight.getArrivalTime() != null ? flight.getArrivalTime() : existing.getArrivalTime(),
                flight.getPrice() != null ? flight.getPrice() : existing.getPrice(),
                flight.getAvailableSeats() != null ? flight.getAvailableSeats() : existing.getAvailableSeats(),
                flight.getStatus() != null ? flight.getStatus() : existing.getStatus()
        );
        return repositoryPort.save(toSave);
    }

    public void delete(Long id) {
        repositoryPort.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + id));
        repositoryPort.deleteById(id);
    }
}

