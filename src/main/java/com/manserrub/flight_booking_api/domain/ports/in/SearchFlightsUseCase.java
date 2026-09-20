package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for searching flights.
 */
public interface SearchFlightsUseCase {
    List<Flight> findByDepartureAirportId(Long airportId);
    
    List<Flight> findByArrivalAirportId(Long airportId);
    
    List<Flight> findByRoute(Long departureAirportId, Long arrivalAirportId);
    
    List<Flight> findAvailableFlights(Long departureAirportId, Long arrivalAirportId, LocalDateTime departureDate);
    
    List<Flight> findByStatus(String status);
}
