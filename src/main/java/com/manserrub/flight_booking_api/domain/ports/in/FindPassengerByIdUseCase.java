package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Passenger;

/**
 * Use case for finding a passenger by ID.
 */
public interface FindPassengerByIdUseCase {
    Passenger execute(Long id);
}
