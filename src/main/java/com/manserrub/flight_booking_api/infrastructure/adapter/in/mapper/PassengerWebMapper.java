package com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper;

import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerPatchRequest;
import org.springframework.stereotype.Component;

@Component
public class PassengerWebMapper {

    public PassengerResponse toResponse(Passenger passenger) {
        return new PassengerResponse(
                passenger.getId(),
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getEmail(),
                "",
                passenger.getPassportNumber(),
                passenger.getBirthDate(),
                passenger.getAge()
        );
    }

    public Passenger toDomain(PassengerRequest request) {
        return new Passenger(
                null,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.passportNumber(),
                request.dateOfBirth()
        );
    }

    public Passenger toDomain(Long id, PassengerRequest request) {
        return new Passenger(
                id,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.passportNumber(),
                request.dateOfBirth()
        );
    }

    public Passenger toDomainPatch(Long id, PassengerPatchRequest request) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        if (request.email() != null && !request.email().isBlank()) {
            passenger.setEmail(request.email());
        }
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            // Note: Passenger domain model doesn't have phoneNumber, but PassengerEntity does
            // This is for partial updates only
        }
        return passenger;
    }
}
