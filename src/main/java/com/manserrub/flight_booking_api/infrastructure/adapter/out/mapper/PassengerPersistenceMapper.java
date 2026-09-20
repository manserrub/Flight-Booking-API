package com.manserrub.flight_booking_api.infrastructure.adapter.out.mapper;

import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.entity.PassengerEntity;
import org.springframework.stereotype.Component;

@Component
public class PassengerPersistenceMapper {

    public Passenger toDomain(PassengerEntity entity) {
        return new Passenger(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPassportNumber(),
                entity.getDateOfBirth()
        );
    }

    public PassengerEntity toEntity(Passenger domain) {
        return new PassengerEntity(
                domain.getId(),
                domain.getFirstName(),
                domain.getLastName(),
                domain.getEmail(),
                domain.getPassportNumber(),
                domain.getPassportNumber(),
                domain.getBirthDate()
        );
    }
}
