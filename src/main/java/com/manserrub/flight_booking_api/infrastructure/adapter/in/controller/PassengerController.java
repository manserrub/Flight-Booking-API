package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.application.service.PassengerService;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerPatchRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.PassengerWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/passengers")
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerWebMapper webMapper;

    public PassengerController(PassengerService passengerService, PassengerWebMapper webMapper) {
        this.passengerService = passengerService;
        this.webMapper = webMapper;
    }

    @GetMapping
    public List<PassengerResponse> findAll() {
        return passengerService.findAll().stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PassengerResponse findById(@PathVariable Long id) {
        return webMapper.toResponse(passengerService.execute(id));
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> create(@Valid @RequestBody PassengerRequest request) {
        var domain = webMapper.toDomain(request);
        var created = passengerService.execute(domain);
        var location = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(webMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public PassengerResponse update(@PathVariable Long id, @Valid @RequestBody PassengerRequest request) {
        var domain = webMapper.toDomain(id, request);
        return webMapper.toResponse(passengerService.execute(id, domain));
    }

    @PatchMapping("/{id}")
    public PassengerResponse patch(@PathVariable Long id, @Valid @RequestBody PassengerPatchRequest request) {
        var domain = webMapper.toDomainPatch(id, request);
        return webMapper.toResponse(passengerService.execute(id, domain));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        passengerService.delete(id);
    }
}
