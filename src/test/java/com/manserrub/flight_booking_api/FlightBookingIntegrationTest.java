package com.manserrub.flight_booking_api;

import com.manserrub.flight_booking_api.application.service.AirportService;
import com.manserrub.flight_booking_api.application.service.BookingService;
import com.manserrub.flight_booking_api.application.service.FlightService;
import com.manserrub.flight_booking_api.application.service.PassengerService;
import com.manserrub.flight_booking_api.domain.exception.SeatNotAvailableException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@DisplayName("Flight Booking Integration Tests")
class FlightBookingIntegrationTest {

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BookingService bookingService;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;
    private Passenger passenger;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport("MAD", "Adolfo Suárez Madrid-Barajas", "Madrid", "Spain");
        arrivalAirport = new Airport("BCN", "Barcelona-El Prat", "Barcelona", "Spain");
        flight = new Flight("AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 10, Flight.FlightStatus.SCHEDULED);
        passenger = new Passenger("John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
    }

    @Nested
    @DisplayName("Complete Booking Workflow Tests")
    class CompleteBookingWorkflow {

        @Test
        @DisplayName("should complete full booking workflow successfully")
        void shouldCompleteFullBookingWorkflowSuccessfully() {
            // Step 1: Create airports
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);

            assertThat(savedDeparture.getId()).isNotNull();
            assertThat(savedArrival.getId()).isNotNull();

            // Step 2: Create flight with new airports
            flight.setDepartureAirport(savedDeparture);
            flight.setArrivalAirport(savedArrival);
            var savedFlight = flightService.create(flight);

            assertThat(savedFlight.getId()).isNotNull();
            assertThat(savedFlight.getFlightNumber()).isEqualTo("AA123");
            assertThat(savedFlight.getAvailableSeats()).isEqualTo(10);

            // Step 3: Create passenger
            var savedPassenger = passengerService.execute(passenger);

            assertThat(savedPassenger.getId()).isNotNull();
            assertThat(savedPassenger.getEmail()).isEqualTo("john@example.com");

            // Step 4: Create booking
            var booking = new Booking(savedFlight, savedPassenger, "12A");
            var savedBooking = bookingService.execute(booking);

            assertThat(savedBooking.getId()).isNotNull();
            assertThat(savedBooking.getBookingReference()).isNotNull().startsWith("BK");
            assertThat(savedBooking.getStatus()).isEqualTo(Booking.BookingStatus.PENDING);

            // Step 5: Verify flight seat was decremented
            var updatedFlight = flightService.execute(savedFlight.getId());
            assertThat(updatedFlight.getAvailableSeats()).isEqualTo(9);

            // Step 6: Confirm booking
            var confirmedBooking = bookingService.findById(savedBooking.getId());
            confirmedBooking.confirm();
            // In a real scenario, we would save this via service

            assertThat(confirmedBooking.getStatus()).isEqualTo(Booking.BookingStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should throw exception when booking flight with no available seats")
        void shouldThrowExceptionWhenBookingFlightWithNoAvailableSeats() {
            // Create airports and flight with zero seats
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);

            var fullFlight = new Flight("BA456", savedDeparture, savedArrival,
                    LocalDateTime.of(2024, 12, 21, 10, 0),
                    LocalDateTime.of(2024, 12, 21, 12, 0),
                    200.0, 0, Flight.FlightStatus.SCHEDULED);
            var savedFlight = flightService.create(fullFlight);

            var savedPassenger = passengerService.execute(passenger);

            var booking = new Booking(savedFlight, savedPassenger, "12A");

            assertThatThrownBy(() -> bookingService.execute(booking))
                    .isInstanceOf(SeatNotAvailableException.class)
                    .hasMessageContaining("No seats available");
        }

        @Test
        @DisplayName("should allow multiple bookings for same flight with different passengers")
        void shouldAllowMultipleBookingsForSameFlightWithDifferentPassengers() {
            // Create airports and flight
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);
            flight.setDepartureAirport(savedDeparture);
            flight.setArrivalAirport(savedArrival);
            var savedFlight = flightService.create(flight);

            // Create multiple passengers
            var passenger1 = passengerService.execute(passenger);
            var passenger2 = new Passenger("Jane", "Smith", "jane@example.com", "B789012",
                    LocalDate.now().minusYears(25));
            var savedPassenger2 = passengerService.execute(passenger2);

            // Create bookings for both passengers
            var booking1 = new Booking(savedFlight, passenger1, "12A");
            var booking2 = new Booking(savedFlight, savedPassenger2, "12B");

            var savedBooking1 = bookingService.execute(booking1);
            var savedBooking2 = bookingService.execute(booking2);

            assertThat(savedBooking1.getId()).isNotNull();
            assertThat(savedBooking2.getId()).isNotNull();

            // Verify seats were decremented twice
            var updatedFlight = flightService.execute(savedFlight.getId());
            assertThat(updatedFlight.getAvailableSeats()).isEqualTo(8);
        }

        @Test
        @DisplayName("should update passenger and flight information")
        void shouldUpdatePassengerAndFlightInformation() {
            // Create initial entities
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);

            flight.setDepartureAirport(savedDeparture);
            flight.setArrivalAirport(savedArrival);
            var savedFlight = flightService.create(flight);

            var savedPassenger = passengerService.execute(passenger);

            // Update passenger
            var updatedPassenger = new Passenger(savedPassenger.getId(), "Jane", "Doe",
                    "jane.doe@example.com", "A123456", LocalDate.now().minusYears(28));
            var modifiedPassenger = passengerService.execute(savedPassenger.getId(), updatedPassenger);

            assertThat(modifiedPassenger.getFirstName()).isEqualTo("Jane");
            assertThat(modifiedPassenger.getEmail()).isEqualTo("jane.doe@example.com");

            // Update flight price
            var updatedFlight = new Flight(savedFlight.getId(), "AA123", savedDeparture, savedArrival,
                    LocalDateTime.of(2024, 12, 20, 10, 0),
                    LocalDateTime.of(2024, 12, 20, 12, 0),
                    200.0, 10, Flight.FlightStatus.SCHEDULED);
            var modifiedFlight = flightService.update(savedFlight.getId(), updatedFlight);

            assertThat(modifiedFlight.getPrice()).isEqualTo(200.0);
        }
    }

    @Nested
    @DisplayName("Data Consistency Tests")
    class DataConsistencyTests {

        @Test
        @DisplayName("should maintain data integrity across operations")
        void shouldMaintainDataIntegrityAcrossOperations() {
            // Create entities
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);
            flight.setDepartureAirport(savedDeparture);
            flight.setArrivalAirport(savedArrival);
            var savedFlight = flightService.create(flight);
            var savedPassenger = passengerService.execute(passenger);

            // Create booking
            var booking = new Booking(savedFlight, savedPassenger, "12A");
            var savedBooking = bookingService.execute(booking);

            // Retrieve and verify
            var retrievedBooking = bookingService.findById(savedBooking.getId());
            var retrievedFlight = flightService.execute(savedFlight.getId());
            var retrievedPassenger = passengerService.execute(savedPassenger.getId());

            assertThat(retrievedBooking.getFlight().getId()).isEqualTo(retrievedFlight.getId());
            assertThat(retrievedBooking.getPassenger().getId()).isEqualTo(retrievedPassenger.getId());
            assertThat(retrievedFlight.getAvailableSeats()).isEqualTo(9);
        }

        @Test
        @DisplayName("should search flights by various criteria")
        void shouldSearchFlightsByVariousCriteria() {
            // Create airports and flights
            var savedDeparture = airportService.createAirport(departureAirport);
            var savedArrival = airportService.createAirport(arrivalAirport);

            flight.setDepartureAirport(savedDeparture);
            flight.setArrivalAirport(savedArrival);
            flightService.create(flight);

            // Search by departure airport
            var flightsByDeparture = flightService.findByDepartureAirportId(savedDeparture.getId());
            assertThat(flightsByDeparture).isNotEmpty();

            // Search by arrival airport
            var flightsByArrival = flightService.findByArrivalAirportId(savedArrival.getId());
            assertThat(flightsByArrival).isNotEmpty();

            // Search by status
            var scheduledFlights = flightService.findByStatus("SCHEDULED");
            assertThat(scheduledFlights).isNotEmpty();
        }
    }
}
