package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.PassengerNotFoundException;
import com.manserrub.flight_booking_api.domain.exception.InvalidPassengerDataException;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.domain.ports.out.PassengerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Passenger Service Tests")
class PassengerServiceTest {

    @Mock
    private PassengerRepositoryPort passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
    }

    @Nested
    @DisplayName("create Tests")
    class CreateTests {

        @Test
        @DisplayName("should create new passenger")
        void shouldCreateNewPassenger() {
            when(passengerRepository.save(passenger)).thenReturn(passenger);

            var result = passengerService.execute(passenger);

            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("John");
            verify(passengerRepository, times(1)).save(passenger);
        }

        @Test
        @DisplayName("should throw exception when email is null")
        void shouldThrowExceptionWhenEmailIsNull() {
            var invalidPassenger = new Passenger(1L, "John", "Doe", "john@example.com", null,
                    LocalDate.now().minusYears(30));

            assertThatThrownBy(() -> passengerService.execute(invalidPassenger))
                    .isInstanceOf(InvalidPassengerDataException.class)
                    .hasMessage("Passport number cannot be null or blank");
        }

        @Test
        @DisplayName("should throw exception when email is blank")
        void shouldThrowExceptionWhenEmailIsBlank() {
            var invalidPassenger = new Passenger(1L, "John", "Doe", "john@example.com", "  ",
                    LocalDate.now().minusYears(30));

            assertThatThrownBy(() -> passengerService.execute(invalidPassenger))
                    .isInstanceOf(InvalidPassengerDataException.class);
        }
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return passenger when found")
        void shouldReturnPassengerWhenFound() {
            when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

            var result = passengerService.execute(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            verify(passengerRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when passenger not found")
        void shouldThrowExceptionWhenPassengerNotFound() {
            when(passengerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passengerService.execute(999L))
                    .isInstanceOf(PassengerNotFoundException.class)
                    .hasMessage("Passenger not found with id: 999");

            verify(passengerRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update existing passenger")
        void shouldUpdateExistingPassenger() {
            var updatedPassenger = new Passenger(1L, "Jane", "Smith", "jane@example.com", "B789012",
                    LocalDate.now().minusYears(25));

            when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
            when(passengerRepository.save(any(Passenger.class))).thenReturn(updatedPassenger);

            var result = passengerService.execute(1L, updatedPassenger);

            assertThat(result).isNotNull();
            assertThat(result.getFirstName()).isEqualTo("Jane");
            verify(passengerRepository, times(1)).findById(1L);
            verify(passengerRepository, times(1)).save(any(Passenger.class));
        }

        @Test
        @DisplayName("should throw exception when passenger not found for update")
        void shouldThrowExceptionWhenPassengerNotFoundForUpdate() {
            when(passengerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passengerService.execute(999L, passenger))
                    .isInstanceOf(PassengerNotFoundException.class);

            verify(passengerRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all passengers")
        void shouldReturnAllPassengers() {
            var passenger2 = new Passenger(2L, "Jane", "Smith", "jane@example.com", "B789012",
                    LocalDate.now().minusYears(25));
            List<Passenger> passengers = Arrays.asList(passenger, passenger2);
            when(passengerRepository.findAll()).thenReturn(passengers);

            var result = passengerService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).contains(passenger, passenger2);
            verify(passengerRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("findByEmail Tests")
    class FindByEmailTests {

        @Test
        @DisplayName("should return passenger by email")
        void shouldReturnPassengerByEmail() {
            when(passengerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(passenger));

            var result = passengerService.findByEmail("john@example.com");

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("john@example.com");
            verify(passengerRepository, times(1)).findByEmail("john@example.com");
        }

        @Test
        @DisplayName("should throw exception when passenger not found by email")
        void shouldThrowExceptionWhenPassengerNotFoundByEmail() {
            when(passengerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passengerService.findByEmail("notfound@example.com"))
                    .isInstanceOf(PassengerNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete existing passenger")
        void shouldDeleteExistingPassenger() {
            when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

            passengerService.delete(1L);

            verify(passengerRepository, times(1)).findById(1L);
            verify(passengerRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when passenger not found for deletion")
        void shouldThrowExceptionWhenPassengerNotFoundForDeletion() {
            when(passengerRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passengerService.delete(999L))
                    .isInstanceOf(PassengerNotFoundException.class);

            verify(passengerRepository, times(1)).findById(999L);
            verify(passengerRepository, never()).deleteById(999L);
        }
    }
}
