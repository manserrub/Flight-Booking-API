package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for Flight repository operations.
 */
public interface FlightRepositoryPort {

    /**
     * Saves a flight (creates or updates).
     */
    Flight save(Flight flight);

    /**
     * Finds a flight by ID.
     */
    Optional<Flight> findById(Long id);

    /**
     * Finds a flight by flight number.
     */
    Optional<Flight> findByFlightNumber(String flightNumber);

    /**
     * Gets all flights.
     */
    List<Flight> findAll();

    /**
     * Finds flights by departure airport.
     */
    List<Flight> findByDepartureAirportId(Long airportId);

    /**
     * Finds flights by arrival airport.
     */
    List<Flight> findByArrivalAirportId(Long airportId);

    /**
     * Finds flights by route (departure and arrival airports).
     */
    List<Flight> findByRoute(Long departureAirportId, Long arrivalAirportId);

    /**
     * Finds flights by status.
     */
    List<Flight> findByStatus(String status);

    /**
     * Finds available flights on a specific route.
     */
    List<Flight> findAvailableFlights(Long departureAirportId, Long arrivalAirportId, LocalDateTime departureDate);

    /**
     * Deletes a flight by ID.
     */
    void deleteById(Long id);

    /**
     * Checks if a flight exists by ID.
     */
    boolean existsById(Long id);

    /**
     * Checks if a flight exists by flight number.
     */
    boolean existsByFlightNumber(String flightNumber);
}

