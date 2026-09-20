package com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa;

import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {
    Optional<BookingEntity> findByBookingReference(String bookingReference);
    List<BookingEntity> findByFlightId(Long flightId);
    List<BookingEntity> findByPassengerId(Long passengerId);
    List<BookingEntity> findByStatus(String status);
    Optional<BookingEntity> findByFlightIdAndSeatNumber(Long flightId, String seatNumber);
    boolean existsByBookingReference(String bookingReference);
}
