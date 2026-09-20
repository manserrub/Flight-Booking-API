package com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper;

import com.manserrub.flight_booking_api.application.service.FlightService;
import com.manserrub.flight_booking_api.application.service.PassengerService;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.BookingRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.BookingResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingWebMapper {

    private final FlightWebMapper flightWebMapper;
    private final PassengerWebMapper passengerWebMapper;
    private final FlightService flightService;
    private final PassengerService passengerService;

    public BookingWebMapper(FlightWebMapper flightWebMapper, PassengerWebMapper passengerWebMapper,
                           FlightService flightService, PassengerService passengerService) {
        this.flightWebMapper = flightWebMapper;
        this.passengerWebMapper = passengerWebMapper;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    public BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getBookingReference(),
                flightWebMapper.toResponse(booking.getFlight()),
                passengerWebMapper.toResponse(booking.getPassenger()),
                booking.getBookingDate(),
                booking.getStatus().name(),
                booking.getSeatNumber()
        );
    }

    public Booking toDomain(BookingRequest request) {
        Flight flight = flightService.execute(request.flightId());
        Passenger passenger = passengerService.execute(request.passengerId());

        return new Booking(
                null,
                null,
                flight,
                passenger,
                LocalDateTime.now(),
                Booking.BookingStatus.PENDING,
                request.seatNumber()
        );
    }
}
