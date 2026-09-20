package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Airport;

/**
 * Use case for creating a new airport.
 */
public interface CreateAirportUseCase {
    Airport execute(Airport airport);
}
