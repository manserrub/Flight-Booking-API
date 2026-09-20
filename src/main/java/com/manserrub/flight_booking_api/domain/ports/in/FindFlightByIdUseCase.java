package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Flight;

/**
 * Use case for finding a flight by ID.
 */
public interface FindFlightByIdUseCase {
    Flight execute(Long id);
}
