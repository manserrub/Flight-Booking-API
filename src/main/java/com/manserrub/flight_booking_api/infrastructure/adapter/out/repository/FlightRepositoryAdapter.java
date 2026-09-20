package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.ports.out.FlightRepositoryPort;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.FlightJpaRepository;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper.FlightPersistenceMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FlightRepositoryAdapter implements FlightRepositoryPort {

    private final FlightJpaRepository jpaRepository;
    private final FlightPersistenceMapper mapper;

    public FlightRepositoryAdapter(FlightJpaRepository jpaRepository, FlightPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Flight save(Flight flight) {
        var entity = mapper.toEntity(flight);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return jpaRepository.findByFlightNumber(flightNumber).map(mapper::toDomain);
    }

    @Override
    public List<Flight> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Flight> findByDepartureAirportId(Long airportId) {
        return jpaRepository.findByDepartureAirportId(airportId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Flight> findByArrivalAirportId(Long airportId) {
        return jpaRepository.findByArrivalAirportId(airportId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Flight> findByRoute(Long departureAirportId, Long arrivalAirportId) {
        return findByDepartureAirportId(departureAirportId).stream()
                .filter(f -> f.getArrivalAirport().getId().equals(arrivalAirportId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Flight> findAvailableFlights(Long departureAirportId, Long arrivalAirportId, LocalDateTime departureDate) {
        return findByRoute(departureAirportId, arrivalAirportId).stream()
                .filter(f -> f.getAvailableSeats() > 0)
                .filter(f -> f.getDepartureTime().toLocalDate().equals(departureDate.toLocalDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return jpaRepository.findByFlightNumber(flightNumber).isPresent();
    }
}
