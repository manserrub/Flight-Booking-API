package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.ports.out.AirportRepositoryPort;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.*;

import com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper.AirportPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AirportRepositoryAdapter implements AirportRepositoryPort {

    private final AirportJpaRepository jpaRepository;
    private final AirportPersistenceMapper mapper;

    public AirportRepositoryAdapter(AirportJpaRepository jpaRepository, AirportPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Airport> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Airport> findByIataCode(String iataCode) {
        return jpaRepository.findByIataCodeIgnoreCase(iataCode).map(mapper::toDomain);
    }

    @Override
    public List<Airport> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Airport> findAllByCity(String city) {
        return jpaRepository.findByCityIgnoreCase(city).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Airport> findAllByCountry(String country) {
        return jpaRepository.findByCountryIgnoreCase(country).stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}
