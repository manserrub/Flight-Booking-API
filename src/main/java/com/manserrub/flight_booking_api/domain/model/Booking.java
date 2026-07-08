package com.manserrub.flight_booking_api.domain.model;

import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private String bookingReference;
    private Flight flight;
    private Passenger passenger;
    private LocalDateTime bookingDate;
    private String status;
    private String seatNumber;
}
