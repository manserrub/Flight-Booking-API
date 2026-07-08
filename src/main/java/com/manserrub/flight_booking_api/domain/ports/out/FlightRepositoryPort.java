package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightRepositoryPort {

    Flight save(Flight flight);

    Optional<Flight> findById(Long id);

    void deleteById(Long id);

    List<Flight> findAll();
}
