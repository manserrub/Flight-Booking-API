package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.AirportJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Airport Repository Adapter Tests")
class AirportRepositoryAdapterTest {

    @Autowired
    private AirportJpaRepository airportJpaRepository;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport("MAD", "Adolfo Suárez Madrid-Barajas", "Madrid", "Spain");
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("should save airport")
        void shouldSaveAirport() {
            var saved = airportJpaRepository.save(airport);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getIataCode()).isEqualTo("MAD");
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should find airport by id")
        void shouldFindAirportById() {
            var saved = airportJpaRepository.save(airport);

            var found = airportJpaRepository.findById(saved.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getIataCode()).isEqualTo("MAD");
        }

        @Test
        @DisplayName("should return empty when airport not found")
        void shouldReturnEmptyWhenAirportNotFound() {
            var found = airportJpaRepository.findById(999L);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should find all airports")
        void shouldFindAllAirports() {
            airportJpaRepository.save(airport);
            var airport2 = new Airport("BCN", "Barcelona-El Prat", "Barcelona", "Spain");
            airportJpaRepository.save(airport2);

            var all = airportJpaRepository.findAll();

            assertThat(all).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete airport")
        void shouldDeleteAirport() {
            var saved = airportJpaRepository.save(airport);

            airportJpaRepository.deleteById(saved.getId());

            var found = airportJpaRepository.findById(saved.getId());
            assertThat(found).isEmpty();
        }
    }
}
