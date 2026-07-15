package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Airport;

import java.util.List;

public interface FindAirportUseCase {
    Airport findById(Long id);
    Airport findByIataCode(String iataCode);

    List<Airport> findAll();
    List<Airport> findAllByCity(String city);
    List<Airport> findAllByCountry(String country);

    Airport createAirport(Airport airport);
    Airport updateAirport(Long id, Airport airport);
    void deleteAirport(Long id);
}
