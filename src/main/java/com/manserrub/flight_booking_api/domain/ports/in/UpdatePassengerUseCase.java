package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Passenger;

/**
 * Use case for updating a passenger.
 */
public interface UpdatePassengerUseCase {
    Passenger execute(Long id, Passenger passenger);
}
