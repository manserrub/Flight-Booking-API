package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.AirportNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.ports.in.FindAirportUseCase;
import com.manserrub.flight_booking_api.domain.ports.out.AirportRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService implements FindAirportUseCase {

    private final AirportRepositoryPort repositoryPort;

    public AirportService(AirportRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Airport findById(Long id) {
        return repositoryPort.findById(id).orElseThrow(() -> new AirportNotFoundException("Airport not found with id: " + id));
    }

    @Override
    public Airport findByIataCode(String iataCode) {
        return repositoryPort.findByIataCode(iataCode).orElseThrow(() -> new AirportNotFoundException("Airport not found with Iata code: " + iataCode));
    }

    @Override
    public List<Airport> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public List<Airport> findAllByCity(String city) {
        return repositoryPort.findAllByCity(city);
    }

    @Override
    public List<Airport> findAllByCountry(String country) {
        return repositoryPort.findAllByCountry(country);
    }
}
