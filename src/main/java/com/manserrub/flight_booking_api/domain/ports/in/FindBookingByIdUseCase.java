package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Booking;

/**
 * Use case for finding a booking by ID.
 */
public interface FindBookingByIdUseCase {
    Booking execute(Long id);
}
