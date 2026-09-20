package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Airport;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Airport repository operations.
 */
public interface AirportRepositoryPort {

    /**
     * Saves an airport (creates or updates).
     */
    Airport save(Airport airport);

    /**
     * Finds an airport by ID.
     */
    Optional<Airport> findById(Long id);

    /**
     * Finds an airport by IATA code.
     */
    Optional<Airport> findByIataCode(String iataCode);

    /**
     * Gets all airports.
     */
    List<Airport> findAll();

    /**
     * Finds airports by city.
     */
    List<Airport> findAllByCity(String city);

    /**
     * Finds airports by country.
     */
    List<Airport> findAllByCountry(String country);

    /**
     * Deletes an airport by ID.
     */
    void deleteById(Long id);

    /**
     * Checks if an airport exists by ID.
     */
    boolean existsById(Long id);

    /**
     * Checks if an airport exists by IATA code.
     */
    boolean existsByIataCode(String iataCode);
}

