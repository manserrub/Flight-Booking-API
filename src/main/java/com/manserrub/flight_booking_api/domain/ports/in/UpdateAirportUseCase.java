package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Airport;

/**
 * Use case for updating an airport.
 */
public interface UpdateAirportUseCase {
    Airport execute(Long id, Airport airport);
}
