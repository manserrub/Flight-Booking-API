package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Passenger;

/**
 * Use case for creating a new passenger.
 */
public interface CreatePassengerUseCase {
    Passenger execute(Passenger passenger);
}
