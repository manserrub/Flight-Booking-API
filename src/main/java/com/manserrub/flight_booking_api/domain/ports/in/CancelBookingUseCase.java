package com.manserrub.flight_booking_api.domain.ports.in;

/**
 * Use case for cancelling a booking.
 */
public interface CancelBookingUseCase {
    void execute(Long id);
}
