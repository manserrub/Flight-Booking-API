package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.FlightJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Flight Repository Adapter Tests")
class FlightRepositoryAdapterTest {

    @Autowired
    private FlightJpaRepository flightJpaRepository;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport("MAD", "Madrid", "Madrid", "Spain");
        arrivalAirport = new Airport("BCN", "Barcelona", "Barcelona", "Spain");
        flight = new Flight("AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 100, Flight.FlightStatus.SCHEDULED);
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("should save flight")
        void shouldSaveFlight() {
            var saved = flightJpaRepository.save(flight);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getFlightNumber()).isEqualTo("AA123");
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should find flight by id")
        void shouldFindFlightById() {
            var saved = flightJpaRepository.save(flight);

            var found = flightJpaRepository.findById(saved.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getFlightNumber()).isEqualTo("AA123");
        }

        @Test
        @DisplayName("should return empty when flight not found")
        void shouldReturnEmptyWhenFlightNotFound() {
            var found = flightJpaRepository.findById(999L);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should find all flights")
        void shouldFindAllFlights() {
            flightJpaRepository.save(flight);
            var flight2 = new Flight("BA456", departureAirport, arrivalAirport,
                    LocalDateTime.of(2024, 12, 20, 14, 0),
                    LocalDateTime.of(2024, 12, 20, 16, 0),
                    200.0, 150, Flight.FlightStatus.SCHEDULED);
            flightJpaRepository.save(flight2);

            var all = flightJpaRepository.findAll();

            assertThat(all).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete flight")
        void shouldDeleteFlight() {
            var saved = flightJpaRepository.save(flight);

            flightJpaRepository.deleteById(saved.getId());

            var found = flightJpaRepository.findById(saved.getId());
            assertThat(found).isEmpty();
        }
    }
}
