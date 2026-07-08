package com.manserrub.flight_booking_api.domain.model;

import java.time.LocalDateTime;

public class Flight {

    private Long id;
    private String flightNumber;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;
    private Integer availableSeats;
    private Integer status;

}
