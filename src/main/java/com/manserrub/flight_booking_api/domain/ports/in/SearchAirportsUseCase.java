package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Airport;

import java.util.List;

/**
 * Use case for searching airports.
 */
public interface SearchAirportsUseCase {
    List<Airport> findAll();
    
    List<Airport> findByCity(String city);
    
    List<Airport> findByCountry(String country);
    
    Airport findByIataCode(String iataCode);
}
