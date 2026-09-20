package com.manserrub.flight_booking_api.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Booking Domain Model Tests")
class BookingTest {

    private final Airport departureAirport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
    private final Airport arrivalAirport = new Airport(2L, "BCN", "Barcelona", "Barcelona", "Spain");
    private final LocalDateTime departureTime = LocalDateTime.of(2024, 12, 20, 10, 0);
    private final LocalDateTime arrivalTime = LocalDateTime.of(2024, 12, 20, 12, 0);
    private final Flight flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
            departureTime, arrivalTime, 150.0, 100, Flight.FlightStatus.SCHEDULED);
    private final Passenger passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456",
            LocalDate.now().minusYears(30));

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create booking with valid data")
        void shouldCreateBookingWithValidData() {
            var booking = new Booking(1L, "BK12345678", flight, passenger,
                    LocalDateTime.now(), Booking.BookingStatus.PENDING, "12A");

            assertThat(booking).isNotNull();
            assertThat(booking.getId()).isEqualTo(1L);
            assertThat(booking.getBookingReference()).isEqualTo("BK12345678");
            assertThat(booking.getFlight()).isEqualTo(flight);
            assertThat(booking.getPassenger()).isEqualTo(passenger);
            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.PENDING);
            assertThat(booking.getSeatNumber()).isEqualTo("12A");
        }

        @Test
        @DisplayName("should create booking with minimal data")
        void shouldCreateBookingWithMinimalData() {
            var booking = new Booking(flight, passenger, "12A");

            assertThat(booking).isNotNull();
            assertThat(booking.getId()).isNull();
            assertThat(booking.getBookingReference()).isNotNull().startsWith("BK");
            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.PENDING);
            assertThat(booking.getBookingDate()).isNotNull();
        }

        @Test
        @DisplayName("should generate unique booking reference when not provided")
        void shouldGenerateUniqueBookingReferenceWhenNotProvided() {
            var booking1 = new Booking(flight, passenger, "12A");
            var booking2 = new Booking(flight, passenger, "12B");

            assertThat(booking1.getBookingReference()).isNotEqualTo(booking2.getBookingReference());
            assertThat(booking1.getBookingReference()).startsWith("BK");
            assertThat(booking2.getBookingReference()).startsWith("BK");
        }

        @Test
        @DisplayName("should set current time as booking date when not provided")
        void shouldSetCurrentTimeAsBookingDateWhenNotProvided() {
            var before = LocalDateTime.now();
            var booking = new Booking(flight, passenger, "12A");
            var after = LocalDateTime.now();

            assertThat(booking.getBookingDate()).isNotNull();
            assertThat(booking.getBookingDate()).isAfterOrEqualTo(before);
            assertThat(booking.getBookingDate()).isBeforeOrEqualTo(after);
        }

        @Test
        @DisplayName("should throw exception when flight is null")
        void shouldThrowExceptionWhenFlightIsNull() {
            assertThatThrownBy(() -> new Booking(null, passenger, "12A"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Flight cannot be null");
        }

        @Test
        @DisplayName("should throw exception when passenger is null")
        void shouldThrowExceptionWhenPassengerIsNull() {
            assertThatThrownBy(() -> new Booking(flight, null, "12A"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Passenger cannot be null");
        }

        @Test
        @DisplayName("should throw exception when seat number is null")
        void shouldThrowExceptionWhenSeatNumberIsNull() {
            assertThatThrownBy(() -> new Booking(flight, passenger, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Seat number cannot be null");
        }

        @Test
        @DisplayName("should throw exception when status is null")
        void shouldThrowExceptionWhenStatusIsNull() {
            assertThatThrownBy(() -> new Booking(1L, "BK12345678", flight, passenger,
                    LocalDateTime.now(), null, "12A"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Status cannot be null");
        }
    }

    @Nested
    @DisplayName("confirm Tests")
    class ConfirmTests {

        @Test
        @DisplayName("should confirm pending booking")
        void shouldConfirmPendingBooking() {
            var booking = new Booking(flight, passenger, "12A");
            booking.confirm();

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should throw exception when confirming cancelled booking")
        void shouldThrowExceptionWhenConfirmingCancelledBooking() {
            var booking = new Booking(flight, passenger, "12A");
            booking.cancel();

            assertThatThrownBy(booking::confirm)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot confirm a cancelled booking");
        }

        @Test
        @DisplayName("should change status from CONFIRMED to CONFIRMED when confirmed again")
        void shouldChangeStatusFromConfirmedToConfirmedWhenConfirmedAgain() {
            var booking = new Booking(flight, passenger, "12A");
            booking.confirm();
            booking.confirm();

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CONFIRMED);
        }
    }

    @Nested
    @DisplayName("cancel Tests")
    class CancelTests {

        @Test
        @DisplayName("should cancel pending booking")
        void shouldCancelPendingBooking() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 10, Flight.FlightStatus.SCHEDULED);
            var booking = new Booking(flight, passenger, "12A");

            booking.cancel();

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CANCELLED);
        }

        @Test
        @DisplayName("should restore seat to flight when booking is cancelled")
        void shouldRestoreSeatToFlightWhenBookingIsCancelled() {
            var flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                    departureTime, arrivalTime, 150.0, 10, Flight.FlightStatus.SCHEDULED);
            var booking = new Booking(flight, passenger, "12A");
            var seatsBeforeCancel = flight.getAvailableSeats();

            booking.cancel();

            assertThat(flight.getAvailableSeats()).isEqualTo(seatsBeforeCancel + 1);
        }

        @Test
        @DisplayName("should throw exception when cancelling already cancelled booking")
        void shouldThrowExceptionWhenCancellingAlreadyCancelledBooking() {
            var booking = new Booking(flight, passenger, "12A");
            booking.cancel();

            assertThatThrownBy(booking::cancel)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Booking is already cancelled");
        }

        @Test
        @DisplayName("should cancel confirmed booking")
        void shouldCancelConfirmedBooking() {
            var booking = new Booking(flight, passenger, "12A");
            booking.confirm();
            booking.cancel();

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CANCELLED);
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("should update booking reference with valid value")
        void shouldUpdateBookingReferenceWithValidValue() {
            var booking = new Booking(flight, passenger, "12A");
            booking.setBookingReference("BK87654321");

            assertThat(booking.getBookingReference()).isEqualTo("BK87654321");
        }

        @Test
        @DisplayName("should generate new reference when setting null")
        void shouldGenerateNewReferenceWhenSettingNull() {
            var booking = new Booking(flight, passenger, "12A");
            var originalReference = booking.getBookingReference();
            booking.setBookingReference(null);

            assertThat(booking.getBookingReference()).isNotNull().startsWith("BK");
            assertThat(booking.getBookingReference()).isNotEqualTo(originalReference);
        }

        @Test
        @DisplayName("should update status with valid value")
        void shouldUpdateStatusWithValidValue() {
            var booking = new Booking(flight, passenger, "12A");
            booking.setStatus(Booking.BookingStatus.CONFIRMED);

            assertThat(booking.getStatus()).isEqualTo(Booking.BookingStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should update seat number with valid value")
        void shouldUpdateSeatNumberWithValidValue() {
            var booking = new Booking(flight, passenger, "12A");
            booking.setSeatNumber("15C");

            assertThat(booking.getSeatNumber()).isEqualTo("15C");
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("should be equal if id and booking reference are the same")
        void shouldBeEqualIfIdAndBookingReferenceAreSame() {
            var booking1 = new Booking(1L, "BK12345678", flight, passenger,
                    LocalDateTime.now(), Booking.BookingStatus.PENDING, "12A");
            var booking2 = new Booking(1L, "BK12345678", flight, passenger,
                    LocalDateTime.now(), Booking.BookingStatus.CONFIRMED, "12B");

            assertThat(booking1).isEqualTo(booking2);
        }

        @Test
        @DisplayName("should not be equal if booking reference differs")
        void shouldNotBeEqualIfBookingReferenceDiffers() {
            var booking1 = new Booking(1L, "BK12345678", flight, passenger,
                    LocalDateTime.now(), Booking.BookingStatus.PENDING, "12A");
            var booking2 = new Booking(1L, "BK87654321", flight, passenger,
                    LocalDateTime.now(), Booking.BookingStatus.PENDING, "12A");

            assertThat(booking1).isNotEqualTo(booking2);
        }
    }
}
