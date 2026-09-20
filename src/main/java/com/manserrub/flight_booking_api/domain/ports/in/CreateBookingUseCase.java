package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Booking;

/**
 * Use case for creating a new booking.
 */
public interface CreateBookingUseCase {
    Booking execute(Booking booking);
}
