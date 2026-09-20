package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Passenger;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Passenger repository operations.
 */
public interface PassengerRepositoryPort {

    /**
     * Saves a passenger (creates or updates).
     */
    Passenger save(Passenger passenger);

    /**
     * Finds a passenger by ID.
     */
    Optional<Passenger> findById(Long id);

    /**
     * Finds a passenger by email.
     */
    Optional<Passenger> findByEmail(String email);

    /**
     * Finds a passenger by passport number.
     */
    Optional<Passenger> findByPassportNumber(String passportNumber);

    /**
     * Finds a passenger by first name and last name.
     */
    Optional<Passenger> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * Gets all passengers.
     */
    List<Passenger> findAll();

    /**
     * Finds passengers by first name.
     */
    List<Passenger> findByFirstName(String firstName);

    /**
     * Finds passengers by last name.
     */
    List<Passenger> findByLastName(String lastName);

    /**
     * Deletes a passenger by ID.
     */
    void deleteById(Long id);

    /**
     * Checks if a passenger exists by ID.
     */
    boolean existsById(Long id);

    /**
     * Checks if a passenger exists by passport number.
     */
    boolean existsByPassportNumber(String passportNumber);

    /**
     * Checks if a passenger exists by email.
     */
    boolean existsByEmail(String email);
}

