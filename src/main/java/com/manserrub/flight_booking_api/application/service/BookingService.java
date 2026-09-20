package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.BookingNotFoundException;
import com.manserrub.flight_booking_api.domain.exception.SeatNotAvailableException;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.ports.in.CreateBookingUseCase;
import com.manserrub.flight_booking_api.domain.ports.in.CancelBookingUseCase;
import com.manserrub.flight_booking_api.domain.ports.out.BookingRepositoryPort;
import com.manserrub.flight_booking_api.domain.ports.out.FlightRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements CreateBookingUseCase, CancelBookingUseCase {

    private final BookingRepositoryPort bookingRepositoryPort;
    private final FlightRepositoryPort flightRepositoryPort;

    public BookingService(BookingRepositoryPort bookingRepositoryPort, FlightRepositoryPort flightRepositoryPort) {
        this.bookingRepositoryPort = bookingRepositoryPort;
        this.flightRepositoryPort = flightRepositoryPort;
    }

    @Override
    public Booking execute(Booking booking) {
        Flight flight = flightRepositoryPort.findById(booking.getFlight().getId())
                .orElseThrow(() -> new SeatNotAvailableException("Flight not found"));

        if (!flight.hasSeatAvailable()) {
            throw new SeatNotAvailableException("No seats available on flight: " + flight.getFlightNumber());
        }

        flight.reserveSeat();
        flightRepositoryPort.save(flight);

        return bookingRepositoryPort.save(booking);
    }

    @Override
    public void execute(Long id) {
        Booking booking = bookingRepositoryPort.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));

        booking.cancel();
        bookingRepositoryPort.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepositoryPort.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }

    public Booking findByReference(String bookingReference) {
        return bookingRepositoryPort.findByBookingReference(bookingReference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + bookingReference));
    }

    public List<Booking> findAll() {
        return bookingRepositoryPort.findAll();
    }

    public List<Booking> findByFlightId(Long flightId) {
        return bookingRepositoryPort.findByFlightId(flightId);
    }

    public List<Booking> findByPassengerId(Long passengerId) {
        return bookingRepositoryPort.findByPassengerId(passengerId);
    }
}
