package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    void deleteById(Long id);

    List<Booking> findAll();
}
