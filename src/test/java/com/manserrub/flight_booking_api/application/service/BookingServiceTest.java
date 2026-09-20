package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.BookingNotFoundException;
import com.manserrub.flight_booking_api.domain.exception.SeatNotAvailableException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.domain.ports.out.BookingRepositoryPort;
import com.manserrub.flight_booking_api.domain.ports.out.FlightRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking Service Tests")
class BookingServiceTest {

    @Mock
    private BookingRepositoryPort bookingRepository;

    @Mock
    private FlightRepositoryPort flightRepository;

    @InjectMocks
    private BookingService bookingService;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;
    private Passenger passenger;
    private Booking booking;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
        arrivalAirport = new Airport(2L, "BCN", "Barcelona", "Barcelona", "Spain");
        flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 10, Flight.FlightStatus.SCHEDULED);
        passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
        booking = new Booking(flight, passenger, "12A");
    }

    @Nested
    @DisplayName("create Tests")
    class CreateTests {

        @Test
        @DisplayName("should create booking when seats are available")
        void shouldCreateBookingWhenSeatsAreAvailable() {
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

            var result = bookingService.execute(booking);

            assertThat(result).isNotNull();
            assertThat(result.getPassenger()).isEqualTo(passenger);
            verify(flightRepository, times(1)).findById(1L);
            verify(flightRepository, times(1)).save(any(Flight.class));
            verify(bookingRepository, times(1)).save(any(Booking.class));
        }

        @Test
        @DisplayName("should throw exception when no seats are available")
        void shouldThrowExceptionWhenNoSeatsAreAvailable() {
            var fullFlight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    LocalDateTime.of(2024, 12, 20, 10, 0),
                    LocalDateTime.of(2024, 12, 20, 12, 0),
                    150.0, 0, Flight.FlightStatus.SCHEDULED);

            when(flightRepository.findById(1L)).thenReturn(Optional.of(fullFlight));

            assertThatThrownBy(() -> bookingService.execute(booking))
                    .isInstanceOf(SeatNotAvailableException.class)
                    .hasMessage("No seats available on flight: AA123");

            verify(flightRepository, times(1)).findById(1L);
            verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        @DisplayName("should throw exception when flight not found")
        void shouldThrowExceptionWhenFlightNotFound() {
            when(flightRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.execute(booking))
                    .isInstanceOf(SeatNotAvailableException.class)
                    .hasMessage("Flight not found");

            verify(flightRepository, times(1)).findById(1L);
            verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        @DisplayName("should decrement available seats when booking is created")
        void shouldDecrementAvailableSeatsWhenBookingIsCreated() {
            var initialSeats = flight.getAvailableSeats();
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

            bookingService.execute(booking);

            assertThat(flight.getAvailableSeats()).isEqualTo(initialSeats - 1);
        }
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return booking when found")
        void shouldReturnBookingWhenFound() {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            var result = bookingService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getPassenger()).isEqualTo(passenger);
            verify(bookingRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when booking not found")
        void shouldThrowExceptionWhenBookingNotFound() {
            when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.findById(999L))
                    .isInstanceOf(BookingNotFoundException.class)
                    .hasMessage("Booking not found with id: 999");

            verify(bookingRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("cancel Tests")
    class CancelTests {

        @Test
        @DisplayName("should cancel existing booking")
        void shouldCancelExistingBooking() {
            booking.setId(1L);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            bookingService.execute(1L);

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CANCELLED);
            verify(bookingRepository, times(1)).findById(1L);
            verify(bookingRepository, times(1)).save(booking);
        }

        @Test
        @DisplayName("should throw exception when booking not found for cancellation")
        void shouldThrowExceptionWhenBookingNotFoundForCancellation() {
            when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.execute(999L))
                    .isInstanceOf(BookingNotFoundException.class);

            verify(bookingRepository, times(1)).findById(999L);
            verify(bookingRepository, never()).save(any(Booking.class));
        }
    }

    @Nested
    @DisplayName("findByReference Tests")
    class FindByReferenceTests {

        @Test
        @DisplayName("should return booking by reference")
        void shouldReturnBookingByReference() {
            when(bookingRepository.findByBookingReference("BK12345678")).thenReturn(Optional.of(booking));

            var result = bookingService.findByReference("BK12345678");

            assertThat(result).isNotNull();
            verify(bookingRepository, times(1)).findByBookingReference("BK12345678");
        }

        @Test
        @DisplayName("should throw exception when booking not found by reference")
        void shouldThrowExceptionWhenBookingNotFoundByReference() {
            when(bookingRepository.findByBookingReference("INVALID")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingService.findByReference("INVALID"))
                    .isInstanceOf(BookingNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all bookings")
        void shouldReturnAllBookings() {
            var booking2 = new Booking(flight, passenger, "12B");
            List<Booking> bookings = Arrays.asList(booking, booking2);
            when(bookingRepository.findAll()).thenReturn(bookings);

            var result = bookingService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).contains(booking, booking2);
            verify(bookingRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("findByFlightId Tests")
    class FindByFlightIdTests {

        @Test
        @DisplayName("should return bookings for flight")
        void shouldReturnBookingsForFlight() {
            List<Booking> bookings = Arrays.asList(booking);
            when(bookingRepository.findByFlightId(1L)).thenReturn(bookings);

            var result = bookingService.findByFlightId(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getFlight().getId()).isEqualTo(1L);
            verify(bookingRepository, times(1)).findByFlightId(1L);
        }
    }

    @Nested
    @DisplayName("findByPassengerId Tests")
    class FindByPassengerIdTests {

        @Test
        @DisplayName("should return bookings for passenger")
        void shouldReturnBookingsForPassenger() {
            List<Booking> bookings = Arrays.asList(booking);
            when(bookingRepository.findByPassengerId(1L)).thenReturn(bookings);

            var result = bookingService.findByPassengerId(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPassenger().getId()).isEqualTo(1L);
            verify(bookingRepository, times(1)).findByPassengerId(1L);
        }
    }
}
