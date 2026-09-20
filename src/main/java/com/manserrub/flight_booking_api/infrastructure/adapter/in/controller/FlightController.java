package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.application.service.FlightService;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.FlightRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.FlightResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.FlightWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;
    private final FlightWebMapper webMapper;

    public FlightController(FlightService flightService, FlightWebMapper webMapper) {
        this.flightService = flightService;
        this.webMapper = webMapper;
    }

    @GetMapping
    public List<FlightResponse> listAll() {
        return flightService.execute().stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FlightResponse findById(@PathVariable Long id) {
        return webMapper.toResponse(flightService.execute(id));
    }

    @GetMapping("/search")
    public List<FlightResponse> search(@RequestParam(required = false) String status) {
        if (status != null) {
            return flightService.findByStatus(status).stream().map(webMapper::toResponse).collect(Collectors.toList());
        }
        return listAll();
    }

    @GetMapping("/departing")
    public List<FlightResponse> searchByDeparture(@RequestParam Long airportId) {
        return flightService.findByDepartureAirportId(airportId).stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/arriving")
    public List<FlightResponse> searchByArrival(@RequestParam Long airportId) {
        return flightService.findByArrivalAirportId(airportId).stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightRequest request) {
        var domain = webMapper.toDomain(request);
        var created = flightService.create(domain);
        var location = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(webMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public FlightResponse update(@PathVariable Long id, @Valid @RequestBody FlightRequest request) {
        var domain = webMapper.toDomain(id, request);
        return webMapper.toResponse(flightService.update(id, domain));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        flightService.delete(id);
    }
}
