package com.manserrub.flight_booking_api.infrastructure.adapter.out.repository;

import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.out.jpa.PassengerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Passenger Repository Adapter Tests")
class PassengerRepositoryAdapterTest {

    @Autowired
    private PassengerJpaRepository passengerJpaRepository;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger("John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("should save passenger")
        void shouldSavePassenger() {
            var saved = passengerJpaRepository.save(passenger);

            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getFirstName()).isEqualTo("John");
        }
    }

    @Nested
    @DisplayName("FindById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should find passenger by id")
        void shouldFindPassengerById() {
            var saved = passengerJpaRepository.save(passenger);

            var found = passengerJpaRepository.findById(saved.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("should return empty when passenger not found")
        void shouldReturnEmptyWhenPassengerNotFound() {
            var found = passengerJpaRepository.findById(999L);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("FindAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should find all passengers")
        void shouldFindAllPassengers() {
            passengerJpaRepository.save(passenger);
            var passenger2 = new Passenger("Jane", "Smith", "jane@example.com", "B789012",
                    LocalDate.now().minusYears(25));
            passengerJpaRepository.save(passenger2);

            var all = passengerJpaRepository.findAll();

            assertThat(all).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete passenger")
        void shouldDeletePassenger() {
            var saved = passengerJpaRepository.save(passenger);

            passengerJpaRepository.deleteById(saved.getId());

            var found = passengerJpaRepository.findById(saved.getId());
            assertThat(found).isEmpty();
        }
    }
}
