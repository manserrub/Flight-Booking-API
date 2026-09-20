package com.manserrub.flight_booking_api.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a booking domain model.
 * This is a domain entity with no Spring framework dependencies.
 */
public class Booking {

    /**
     * Enum for booking status.
     */
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }

    private Long id;
    private String bookingReference;
    private Flight flight;
    private Passenger passenger;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private String seatNumber;

    /**
     * Default constructor required for framework instantiation.
     */
    public Booking() {
    }

    /**
     * Constructor with all fields.
     */
    public Booking(Long id, String bookingReference, Flight flight, Passenger passenger,
                   LocalDateTime bookingDate, BookingStatus status, String seatNumber) {
        this.id = id;
        this.bookingReference = bookingReference != null ? bookingReference : generateBookingReference();
        this.flight = Objects.requireNonNull(flight, "Flight cannot be null");
        this.passenger = Objects.requireNonNull(passenger, "Passenger cannot be null");
        this.bookingDate = bookingDate != null ? bookingDate : LocalDateTime.now();
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.seatNumber = Objects.requireNonNull(seatNumber, "Seat number cannot be null");
    }

    /**
     * Constructor without ID and booking reference (for new bookings).
     */
    public Booking(Flight flight, Passenger passenger, String seatNumber) {
        this(null, null, flight, passenger, LocalDateTime.now(), BookingStatus.PENDING, seatNumber);
    }

    /**
     * Generates a unique booking reference.
     */
    private static String generateBookingReference() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference != null ? bookingReference : generateBookingReference();
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = Objects.requireNonNull(flight, "Flight cannot be null");
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = Objects.requireNonNull(passenger, "Passenger cannot be null");
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate != null ? bookingDate : LocalDateTime.now();
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = Objects.requireNonNull(seatNumber, "Seat number cannot be null");
    }

    /**
     * Confirms the booking.
     */
    public void confirm() {
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot confirm a cancelled booking");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * Cancels the booking.
     */
    public void cancel() {
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }
        this.status = BookingStatus.CANCELLED;
        // When cancelled, add the seat back to available seats
        if (flight != null) {
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(bookingReference, booking.bookingReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingReference);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingReference='" + bookingReference + '\'' +
                ", flight=" + (flight != null ? flight.getFlightNumber() : null) +
                ", passenger=" + (passenger != null ? passenger.getFullName() : null) +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
