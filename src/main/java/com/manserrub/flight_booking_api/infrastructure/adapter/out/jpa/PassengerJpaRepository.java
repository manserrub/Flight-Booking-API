package com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa;

import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerJpaRepository extends JpaRepository<PassengerEntity, Long> {
    Optional<PassengerEntity> findByEmail(String email);
    Optional<PassengerEntity> findByPassportNumber(String passportNumber);
    Optional<PassengerEntity> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    List<PassengerEntity> findByFirstNameIgnoreCase(String firstName);
    List<PassengerEntity> findByLastNameIgnoreCase(String lastName);
    boolean existsByEmail(String email);
    boolean existsByPassportNumber(String passportNumber);
}
