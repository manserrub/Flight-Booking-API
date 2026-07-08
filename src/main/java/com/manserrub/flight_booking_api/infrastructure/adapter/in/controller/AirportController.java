package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.domain.ports.in.FindAirportUseCase;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.AirportWebMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    private final FindAirportUseCase findAirportUseCase;
    private final AirportWebMapper webMapper;

    public AirportController(FindAirportUseCase findAirportUseCase, AirportWebMapper webMapper) {
        this.findAirportUseCase = findAirportUseCase;
        this.webMapper = webMapper;
    }

    @GetMapping
    public List<AirportResponse> findAll() {
        return findAirportUseCase.findAll().stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AirportResponse findById(@PathVariable Long id) {
        return webMapper.toResponse(findAirportUseCase.findById(id));
    }

    @GetMapping("/iata/{iataCode}")
    public AirportResponse findByIataCode(@PathVariable String iataCode) {
        return webMapper.toResponse(findAirportUseCase.findByIataCode(iataCode));
    }

    @GetMapping("/search")
    public List<AirportResponse> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country) {
        if (city != null) {
            return findAirportUseCase.findAllByCity(city).stream().map(webMapper::toResponse).collect(Collectors.toList());
        }
        if (country != null) {
            return findAirportUseCase.findAllByCountry(country).stream().map(webMapper::toResponse).collect(Collectors.toList());
        }
        return findAll();
    }
}
