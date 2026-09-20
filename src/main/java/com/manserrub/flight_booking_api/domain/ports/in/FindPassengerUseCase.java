package com.manserrub.flight_booking_api.domain.ports.in;

import com.manserrub.flight_booking_api.domain.model.Passenger;

import java.util.List;
import java.util.Optional;

public interface FindPassengerUseCase {
    Passenger findById(Long id);
    Passenger findByEmail(String email);
    Passenger findByPassportNumber(String passportNumber);
    Passenger findByFirstNameAndLastName(String firstName, String lastName);

    List<Passenger> findAll();
    Passenger createPassenger(Passenger passenger);
    void deletePassengerById(Long id);
}
