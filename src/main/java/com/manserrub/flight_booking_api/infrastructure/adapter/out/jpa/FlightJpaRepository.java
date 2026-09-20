package com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa;

import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlightJpaRepository extends JpaRepository<FlightEntity, Long> {
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
    List<FlightEntity> findByDepartureAirportId(Long departureAirportId);
    List<FlightEntity> findByArrivalAirportId(Long arrivalAirportId);
    List<FlightEntity> findByStatus(String status);
}
