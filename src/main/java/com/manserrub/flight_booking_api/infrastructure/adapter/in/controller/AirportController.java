package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.domain.ports.in.FindAirportUseCase;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportRequest;
import jakarta.validation.Valid;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.AirportWebMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@Valid @RequestBody AirportRequest request) {
        var domain = webMapper.toDomain(request);
        var created = findAirportUseCase.createAirport(domain);
        var location = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(webMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public AirportResponse updateAirport(@PathVariable Long id, @Valid @RequestBody AirportRequest request) {
        var domain = webMapper.toDomain(id, request);
        return webMapper.toResponse(findAirportUseCase.updateAirport(id, domain));
    }

    @PatchMapping("/{id}")
    public AirportResponse patchAirport(@PathVariable Long id, @Valid @RequestBody com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportPatchRequest request) {
        var domain = webMapper.toDomainPatch(id, request);
        return webMapper.toResponse(findAirportUseCase.updateAirport(id, domain));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAirport(@PathVariable Long id) {
        findAirportUseCase.deleteAirport(id);
    }
}
