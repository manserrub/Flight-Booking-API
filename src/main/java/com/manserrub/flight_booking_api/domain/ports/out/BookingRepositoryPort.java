package com.manserrub.flight_booking_api.domain.ports.out;

import com.manserrub.flight_booking_api.domain.model.Booking;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Booking repository operations.
 */
public interface BookingRepositoryPort {

    /**
     * Saves a booking (creates or updates).
     */
    Booking save(Booking booking);

    /**
     * Finds a booking by ID.
     */
    Optional<Booking> findById(Long id);

    /**
     * Finds a booking by booking reference.
     */
    Optional<Booking> findByBookingReference(String bookingReference);

    /**
     * Gets all bookings.
     */
    List<Booking> findAll();

    /**
     * Finds bookings by flight ID.
     */
    List<Booking> findByFlightId(Long flightId);

    /**
     * Finds bookings by passenger ID.
     */
    List<Booking> findByPassengerId(Long passengerId);

    /**
     * Finds bookings by status.
     */
    List<Booking> findByStatus(String status);

    /**
     * Finds bookings by seat number (for a specific flight).
     */
    Optional<Booking> findBySeatNumber(String seatNumber);

    /**
     * Deletes a booking by ID.
     */
    void deleteById(Long id);

    /**
     * Checks if a booking exists by ID.
     */
    boolean existsById(Long id);

    /**
     * Checks if a booking exists by booking reference.
     */
    boolean existsByBookingReference(String bookingReference);

    /**
     * Checks if a seat is booked on a flight.
     */
    boolean isSeatBooked(Long flightId, String seatNumber);
}

