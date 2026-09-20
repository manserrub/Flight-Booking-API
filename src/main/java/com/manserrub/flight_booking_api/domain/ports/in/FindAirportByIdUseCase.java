package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Airport;

/**
 * Use case for finding an airport by ID.
 */
public interface FindAirportByIdUseCase {
    Airport execute(Long id);
}
