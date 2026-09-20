package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.domain.ports.out.PassengerRepositoryPort;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.PassengerJpaRepository;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper.PassengerPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PassengerRepositoryAdapter implements PassengerRepositoryPort {

    private final PassengerJpaRepository jpaRepository;
    private final PassengerPersistenceMapper mapper;

    public PassengerRepositoryAdapter(PassengerJpaRepository jpaRepository, PassengerPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Passenger save(Passenger passenger) {
        var entity = mapper.toEntity(passenger);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Passenger> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Passenger> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Passenger> findByPassportNumber(String passportNumber) {
        return jpaRepository.findByPassportNumber(passportNumber).map(mapper::toDomain);
    }

    @Override
    public Optional<Passenger> findByFirstNameAndLastName(String firstName, String lastName) {
        return jpaRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).map(mapper::toDomain);
    }

    @Override
    public List<Passenger> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Passenger> findByFirstName(String firstName) {
        return jpaRepository.findByFirstNameIgnoreCase(firstName).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Passenger> findByLastName(String lastName) {
        return jpaRepository.findByLastNameIgnoreCase(lastName).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByPassportNumber(String passportNumber) {
        return jpaRepository.existsByPassportNumber(passportNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
