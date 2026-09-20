package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.InvalidPassengerDataException;
import com.manserrub.flight_booking_api.domain.exception.PassengerNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.domain.ports.in.CreatePassengerUseCase;
import com.manserrub.flight_booking_api.domain.ports.in.FindPassengerByIdUseCase;
import com.manserrub.flight_booking_api.domain.ports.in.UpdatePassengerUseCase;
import com.manserrub.flight_booking_api.domain.ports.out.PassengerRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService implements CreatePassengerUseCase, FindPassengerByIdUseCase, UpdatePassengerUseCase {

    private final PassengerRepositoryPort repositoryPort;

    public PassengerService(PassengerRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Passenger execute(Passenger passenger) {
        if (passenger.getEmail() == null || passenger.getEmail().isBlank()) {
            throw new InvalidPassengerDataException("Email cannot be null or blank");
        }
        if (passenger.getPassportNumber() == null || passenger.getPassportNumber().isBlank()) {
            throw new InvalidPassengerDataException("Passport number cannot be null or blank");
        }
        return repositoryPort.save(passenger);
    }

    @Override
    public Passenger execute(Long id) {
        return repositoryPort.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger not found with id: " + id));
    }

    @Override
    public Passenger execute(Long id, Passenger passenger) {
        Passenger existing = repositoryPort.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger not found with id: " + id));
        Passenger toSave = new Passenger(
                existing.getId(),
                passenger.getFirstName() != null ? passenger.getFirstName() : existing.getFirstName(),
                passenger.getLastName() != null ? passenger.getLastName() : existing.getLastName(),
                passenger.getEmail() != null ? passenger.getEmail() : existing.getEmail(),
                passenger.getPassportNumber() != null ? passenger.getPassportNumber() : existing.getPassportNumber(),
                passenger.getBirthDate() != null ? passenger.getBirthDate() : existing.getBirthDate()
        );
        return repositoryPort.save(toSave);
    }

    public List<Passenger> findAll() {
        return repositoryPort.findAll();
    }

    public Passenger findByEmail(String email) {
        return repositoryPort.findByEmail(email).orElseThrow(() -> new PassengerNotFoundException("Passenger not found with email: " + email));
    }

    public Passenger findByPassportNumber(String passportNumber) {
        return repositoryPort.findByPassportNumber(passportNumber).orElseThrow(() -> new PassengerNotFoundException("Passenger not found with passport number: " + passportNumber));
    }

    public void delete(Long id) {
        repositoryPort.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger not found with id: " + id));
        repositoryPort.deleteById(id);
    }
}
