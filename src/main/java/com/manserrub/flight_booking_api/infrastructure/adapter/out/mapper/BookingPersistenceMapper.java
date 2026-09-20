package com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper;

import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.BookingEntity;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.FlightEntity;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.PassengerEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingPersistenceMapper {

    private final FlightPersistenceMapper flightMapper;
    private final PassengerPersistenceMapper passengerMapper;

    public BookingPersistenceMapper(FlightPersistenceMapper flightMapper, PassengerPersistenceMapper passengerMapper) {
        this.flightMapper = flightMapper;
        this.passengerMapper = passengerMapper;
    }

    public Booking toDomain(BookingEntity entity) {
        Flight flight = flightMapper.toDomain(entity.getFlight());
        Passenger passenger = passengerMapper.toDomain(entity.getPassenger());

        return new Booking(
                entity.getId(),
                entity.getBookingReference(),
                flight,
                passenger,
                entity.getBookingDate(),
                Booking.BookingStatus.valueOf(entity.getStatus()),
                entity.getSeatNumber()
        );
    }

    public BookingEntity toEntity(Booking domain) {
        FlightEntity flight = flightMapper.toEntity(domain.getFlight());
        PassengerEntity passenger = passengerMapper.toEntity(domain.getPassenger());

        return new BookingEntity(
                domain.getId(),
                domain.getBookingReference(),
                flight,
                passenger,
                domain.getBookingDate(),
                domain.getStatus().name(),
                domain.getSeatNumber()
        );
    }
}
