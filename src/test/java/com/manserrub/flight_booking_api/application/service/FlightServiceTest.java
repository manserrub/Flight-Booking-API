package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.FlightNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Flight;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Flight Service Tests")
class FlightServiceTest {

    @Mock
    private FlightRepositoryPort flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
        arrivalAirport = new Airport(2L, "BCN", "Barcelona", "Barcelona", "Spain");
        flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 100, Flight.FlightStatus.SCHEDULED);
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return flight when found")
        void shouldReturnFlightWhenFound() {
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

            var result = flightService.execute(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFlightNumber()).isEqualTo("AA123");
            verify(flightRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when flight not found")
        void shouldThrowExceptionWhenFlightNotFound() {
            when(flightRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> flightService.execute(999L))
                    .isInstanceOf(FlightNotFoundException.class)
                    .hasMessage("Flight not found with id: 999");

            verify(flightRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all flights")
        void shouldReturnAllFlights() {
            var flight2 = new Flight(2L, "BA456", departureAirport, arrivalAirport,
                    LocalDateTime.of(2024, 12, 20, 14, 0),
                    LocalDateTime.of(2024, 12, 20, 16, 0),
                    200.0, 150, Flight.FlightStatus.SCHEDULED);
            List<Flight> flights = Arrays.asList(flight, flight2);
            when(flightRepository.findAll()).thenReturn(flights);

            var result = flightService.execute();

            assertThat(result).hasSize(2);
            assertThat(result).contains(flight, flight2);
            verify(flightRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("should return empty list when no flights")
        void shouldReturnEmptyListWhenNoFlights() {
            when(flightRepository.findAll()).thenReturn(Arrays.asList());

            var result = flightService.execute();

            assertThat(result).isEmpty();
            verify(flightRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("findByDepartureAirportId Tests")
    class FindByDepartureAirportIdTests {

        @Test
        @DisplayName("should return flights departing from airport")
        void shouldReturnFlightsDepartingFromAirport() {
            List<Flight> flights = Arrays.asList(flight);
            when(flightRepository.findByDepartureAirportId(1L)).thenReturn(flights);

            var result = flightService.findByDepartureAirportId(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getDepartureAirport().getId()).isEqualTo(1L);
            verify(flightRepository, times(1)).findByDepartureAirportId(1L);
        }
    }

    @Nested
    @DisplayName("findByStatus Tests")
    class FindByStatusTests {

        @Test
        @DisplayName("should return flights with specified status")
        void shouldReturnFlightsWithSpecifiedStatus() {
            List<Flight> flights = Arrays.asList(flight);
            when(flightRepository.findByStatus("SCHEDULED")).thenReturn(flights);

            var result = flightService.findByStatus("SCHEDULED");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStatus()).isEqualTo(Flight.FlightStatus.SCHEDULED);
            verify(flightRepository, times(1)).findByStatus("SCHEDULED");
        }
    }

    @Nested
    @DisplayName("create Tests")
    class CreateTests {

        @Test
        @DisplayName("should create new flight")
        void shouldCreateNewFlight() {
            when(flightRepository.save(flight)).thenReturn(flight);

            var result = flightService.create(flight);

            assertThat(result).isNotNull();
            assertThat(result.getFlightNumber()).isEqualTo("AA123");
            verify(flightRepository, times(1)).save(flight);
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update existing flight")
        void shouldUpdateExistingFlight() {
            var updatedFlight = new Flight(1L, "AA124", departureAirport, arrivalAirport,
                    LocalDateTime.of(2024, 12, 20, 10, 0),
                    LocalDateTime.of(2024, 12, 20, 12, 0),
                    200.0, 100, Flight.FlightStatus.SCHEDULED);

            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

            var result = flightService.update(1L, updatedFlight);

            assertThat(result).isNotNull();
            assertThat(result.getFlightNumber()).isEqualTo("AA124");
            verify(flightRepository, times(1)).findById(1L);
            verify(flightRepository, times(1)).save(any(Flight.class));
        }

        @Test
        @DisplayName("should throw exception when flight not found for update")
        void shouldThrowExceptionWhenFlightNotFoundForUpdate() {
            when(flightRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> flightService.update(999L, flight))
                    .isInstanceOf(FlightNotFoundException.class);

            verify(flightRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete existing flight")
        void shouldDeleteExistingFlight() {
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

            flightService.delete(1L);

            verify(flightRepository, times(1)).findById(1L);
            verify(flightRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when flight not found for deletion")
        void shouldThrowExceptionWhenFlightNotFoundForDeletion() {
            when(flightRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> flightService.delete(999L))
                    .isInstanceOf(FlightNotFoundException.class);

            verify(flightRepository, times(1)).findById(999L);
            verify(flightRepository, never()).deleteById(999L);
        }
    }
}
