package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.application.service.BookingService;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.BookingRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.BookingResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.BookingWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingWebMapper webMapper;

    public BookingController(BookingService bookingService, BookingWebMapper webMapper) {
        this.bookingService = bookingService;
        this.webMapper = webMapper;
    }

    @GetMapping
    public List<BookingResponse> findAll() {
        return bookingService.findAll().stream().map(webMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        return webMapper.toResponse(bookingService.findById(id));
    }

    @GetMapping("/reference/{reference}")
    public BookingResponse findByReference(@PathVariable String reference) {
        return webMapper.toResponse(bookingService.findByReference(reference));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        var domain = webMapper.toDomain(request);
        var created = bookingService.execute(domain);
        var location = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(webMapper.toResponse(created));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        bookingService.execute(id);
    }
}
