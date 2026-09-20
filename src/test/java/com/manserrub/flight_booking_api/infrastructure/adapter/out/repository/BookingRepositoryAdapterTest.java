package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.BookingJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Booking Repository Adapter Tests")
class BookingRepositoryAdapterTest {

    @Autowired
    private BookingJpaRepository bookingJpaRepository;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;
    private Passenger passenger;
    private Booking booking;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport("MAD", "Madrid", "Madrid", "Spain");
        arrivalAirport = new Airport("BCN", "Barcelona", "Barcelona", "Spain");
        flight = new Flight("AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 100, Flight.FlightStatus.SCHEDULED);
        passenger = new Passenger("John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
        booking = new Booking(flight, passenger, "12A");
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("should save booking")
        void shouldSaveBooking() {
            var saved = bookingJpaRepository.save(booking);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getSeatNumber()).isEqualTo("12A");
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should find booking by id")
        void shouldFindBookingById() {
            var saved = bookingJpaRepository.save(booking);

            var found = bookingJpaRepository.findById(saved.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getSeatNumber()).isEqualTo("12A");
        }

        @Test
        @DisplayName("should return empty when booking not found")
        void shouldReturnEmptyWhenBookingNotFound() {
            var found = bookingJpaRepository.findById(999L);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should find all bookings")
        void shouldFindAllBookings() {
            bookingJpaRepository.save(booking);
            var booking2 = new Booking(flight, passenger, "12B");
            bookingJpaRepository.save(booking2);

            var all = bookingJpaRepository.findAll();

            assertThat(all).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete booking")
        void shouldDeleteBooking() {
            var saved = bookingJpaRepository.save(booking);

            bookingJpaRepository.deleteById(saved.getId());

            var found = bookingJpaRepository.findById(saved.getId());
            assertThat(found).isEmpty();
        }
    }
}
