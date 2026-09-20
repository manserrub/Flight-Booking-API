package com.manserrub.flight_booking_api.domain.ports.in;

/**
 * Use case for deleting an airport.
 */
public interface DeleteAirportUseCase {
    void execute(Long id);
}
