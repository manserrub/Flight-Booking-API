package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportRepositoryPort {

    Optional<Airport> findById(Long id);
    Optional<Airport> findByIataCode(String iataCode);

    List<Airport> findAll();
    List<Airport> findAllByCity(String city);
    List<Airport> findAllByCountry(String country);

    Airport save(Airport airport);
    void deleteById(Long id);
}
