package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.ports.out.BookingRepositoryPort;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.BookingJpaRepository;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper.BookingPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookingRepositoryAdapter implements BookingRepositoryPort {

    private final BookingJpaRepository jpaRepository;
    private final BookingPersistenceMapper mapper;

    public BookingRepositoryAdapter(BookingJpaRepository jpaRepository, BookingPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Booking save(Booking booking) {
        var entity = mapper.toEntity(booking);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Booking> findByBookingReference(String bookingReference) {
        return jpaRepository.findByBookingReference(bookingReference).map(mapper::toDomain);
    }

    @Override
    public List<Booking> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByFlightId(Long flightId) {
        return jpaRepository.findByFlightId(flightId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByPassengerId(Long passengerId) {
        return jpaRepository.findByPassengerId(passengerId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> findBySeatNumber(String seatNumber) {
        // Find any booking with the given seat number (may need to filter by flight in a real scenario)
        return findAll().stream()
                .filter(b -> seatNumber.equals(b.getSeatNumber()))
                .findFirst();
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
    public boolean existsByBookingReference(String bookingReference) {
        return jpaRepository.existsByBookingReference(bookingReference);
    }

    @Override
    public boolean isSeatBooked(Long flightId, String seatNumber) {
        return jpaRepository.findByFlightIdAndSeatNumber(flightId, seatNumber).isPresent();
    }
}
