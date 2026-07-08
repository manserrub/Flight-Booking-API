package com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa;

import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportJpaRepository extends JpaRepository<AirportEntity, Long> {
    Optional<AirportEntity> findByIataCodeIgnoreCase(String iataCode);
    List<AirportEntity> findByCityIgnoreCase(String city);
    List<AirportEntity> findByCountryIgnoreCase(String country);
}
