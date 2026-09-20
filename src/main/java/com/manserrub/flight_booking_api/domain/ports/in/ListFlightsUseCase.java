package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Flight;

import java.util.List;

/**
 * Use case for listing flights.
 */
public interface ListFlightsUseCase {
    List<Flight> execute();
}
