package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerRepositoryPort {
    Passenger save(Passenger passenger);

    Optional<Passenger> findById(Long id);

    void deleteById(Long id);

    List<Passenger> findAll();
}
