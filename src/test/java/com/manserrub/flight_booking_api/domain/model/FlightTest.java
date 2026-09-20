package com.manserrub.flight_booking_api.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Flight Domain Model Tests")
class FlightTest {

    private final Airport departureAirport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
    private final Airport arrivalAirport = new Airport(2L, "BCN", "Barcelona", "Barcelona", "Spain");
    private final LocalDateTime departureTime = LocalDateTime.of(2024, 12, 20, 10, 0);
    private final LocalDateTime arrivalTime = LocalDateTime.of(2024, 12, 20, 12, 0);

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create flight with valid data")
        void shouldCreateFlightWithValidData() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            assertThat(flight).isNotNull();
            assertThat(flight.getId()).isEqualTo(1L);
            assertThat(flight.getFlightNumber()).isEqualTo("AA123");
            assertThat(flight.getPrice()).isEqualTo(150.0);
            assertThat(flight.getAvailableSeats()).isEqualTo(100);
            assertThat(flight.getStatus()).isEqualTo(Flight.FlightStatus.SCHEDULED);
        }

        @Test
        @DisplayName("should create flight without ID")
        void shouldCreateFlightWithoutId() {
            var flight = new Flight("BA456", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 200.0, 150, Flight.FlightStatus.SCHEDULED);

            assertThat(flight).isNotNull();
            assertThat(flight.getId()).isNull();
            assertThat(flight.getFlightNumber()).isEqualTo("BA456");
        }

        @Test
        @DisplayName("should throw exception when flight number is null")
        void shouldThrowExceptionWhenFlightNumberIsNull() {
            assertThatThrownBy(() -> new Flight(1L, null, departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Flight number cannot be null");
        }

        @Test
        @DisplayName("should throw exception when flight number format is invalid")
        void shouldThrowExceptionWhenFlightNumberFormatIsInvalid() {
            assertThatThrownBy(() -> new Flight(1L, "123AA", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Flight number must be in format: 2 uppercase letters + 3-4 digits");

            assertThatThrownBy(() -> new Flight(1L, "aa123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should throw exception when departure airport is null")
        void shouldThrowExceptionWhenDepartureAirportIsNull() {
            assertThatThrownBy(() -> new Flight(1L, "AA123", null, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Departure airport cannot be null");
        }

        @Test
        @DisplayName("should throw exception when arrival airport is null")
        void shouldThrowExceptionWhenArrivalAirportIsNull() {
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, null,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Arrival airport cannot be null");
        }

        @Test
        @DisplayName("should throw exception when departure time is after arrival time")
        void shouldThrowExceptionWhenDepartureTimeIsAfterArrivalTime() {
            var invalidArrivalTime = LocalDateTime.of(2024, 12, 20, 8, 0);
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, invalidArrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Departure time must be before arrival time");
        }

        @Test
        @DisplayName("should throw exception when departure time equals arrival time")
        void shouldThrowExceptionWhenDepartureTimeEqualsArrivalTime() {
            var sameTime = LocalDateTime.of(2024, 12, 20, 10, 0);
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    sameTime, sameTime, 150.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Departure time must be before arrival time");
        }

        @Test
        @DisplayName("should throw exception when price is negative")
        void shouldThrowExceptionWhenPriceIsNegative() {
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, -10.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must be greater than 0");
        }

        @Test
        @DisplayName("should throw exception when price is zero")
        void shouldThrowExceptionWhenPriceIsZero() {
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 0.0, 100, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Price must be greater than 0");
        }

        @Test
        @DisplayName("should throw exception when available seats is negative")
        void shouldThrowExceptionWhenAvailableSeatsIsNegative() {
            assertThatThrownBy(() -> new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, -5, Flight.FlightStatus.SCHEDULED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Available seats cannot be negative");
        }

        @Test
        @DisplayName("should create flight with zero available seats")
        void shouldCreateFlightWithZeroAvailableSeats() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 0, Flight.FlightStatus.SCHEDULED);

            assertThat(flight.getAvailableSeats()).isZero();
        }
    }

    @Nested
    @DisplayName("hasSeatAvailable Tests")
    class HasSeatAvailableTests {

        @Test
        @DisplayName("should return true when seats are available")
        void shouldReturnTrueWhenSeatsAreAvailable() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 5, Flight.FlightStatus.SCHEDULED);

            assertThat(flight.hasSeatAvailable()).isTrue();
        }

        @Test
        @DisplayName("should return false when no seats are available")
        void shouldReturnFalseWhenNoSeatsAreAvailable() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 0, Flight.FlightStatus.SCHEDULED);

            assertThat(flight.hasSeatAvailable()).isFalse();
        }
    }

    @Nested
    @DisplayName("reserveSeat Tests")
    class ReserveSeatTests {

        @Test
        @DisplayName("should decrement available seats when seat is reserved")
        void shouldDecrementAvailableSeatsWhenSeatIsReserved() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 5, Flight.FlightStatus.SCHEDULED);

            flight.reserveSeat();

            assertThat(flight.getAvailableSeats()).isEqualTo(4);
        }

        @Test
        @DisplayName("should throw exception when no seats are available")
        void shouldThrowExceptionWhenNoSeatsAreAvailable() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 0, Flight.FlightStatus.SCHEDULED);

            assertThatThrownBy(flight::reserveSeat)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("No seats available on this flight");
        }

        @Test
        @DisplayName("should reserve multiple seats sequentially")
        void shouldReserveMultipleSeatsSequentially() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 3, Flight.FlightStatus.SCHEDULED);

            flight.reserveSeat();
            flight.reserveSeat();
            flight.reserveSeat();

            assertThat(flight.getAvailableSeats()).isZero();
            assertThat(flight.hasSeatAvailable()).isFalse();
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("should update flight number with valid value")
        void shouldUpdateFlightNumberWithValidValue() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            flight.setFlightNumber("BA456");

            assertThat(flight.getFlightNumber()).isEqualTo("BA456");
        }

        @Test
        @DisplayName("should throw exception when setting invalid flight number")
        void shouldThrowExceptionWhenSettingInvalidFlightNumber() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            assertThatThrownBy(() -> flight.setFlightNumber("INVALID"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should update price with valid value")
        void shouldUpdatePriceWithValidValue() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            flight.setPrice(250.0);

            assertThat(flight.getPrice()).isEqualTo(250.0);
        }

        @Test
        @DisplayName("should throw exception when setting invalid price")
        void shouldThrowExceptionWhenSettingInvalidPrice() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            assertThatThrownBy(() -> flight.setPrice(0.0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should update status with valid value")
        void shouldUpdateStatusWithValidValue() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            flight.setStatus(Flight.FlightStatus.DEPARTED);

            assertThat(flight.getStatus()).isEqualTo(Flight.FlightStatus.DEPARTED);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("should be equal if id and flight number are the same")
        void shouldBeEqualIfIdAndFlightNumberAreSame() {
            var flight1 = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);
            var flight2 = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 200.0, 50, Flight.FlightStatus.CANCELLED);

            assertThat(flight1).isEqualTo(flight2);
        }

        @Test
        @DisplayName("should not be equal if flight number differs")
        void shouldNotBeEqualIfFlightNumberDiffers() {
            var flight1 = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);
            var flight2 = new Flight(1L, "BA456", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);

            assertThat(flight1).isNotEqualTo(flight2);
        }
    }
}
