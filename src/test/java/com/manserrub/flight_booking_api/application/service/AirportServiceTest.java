package com.manserrub.flight_booking_api.application.service;

import com.manserrub.flight_booking_api.domain.exception.AirportNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.ports.out.AirportRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Airport Service Tests")
class AirportServiceTest {

    @Mock
    private AirportRepositoryPort airportRepository;

    @InjectMocks
    private AirportService airportService;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = new Airport(1L, "MAD", "Adolfo Suárez Madrid-Barajas", "Madrid", "Spain");
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return airport when found")
        void shouldReturnAirportWhenFound() {
            when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));

            var result = airportService.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getIataCode()).isEqualTo("MAD");
            verify(airportRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should throw exception when airport not found")
        void shouldThrowExceptionWhenAirportNotFound() {
            when(airportRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> airportService.findById(999L))
                    .isInstanceOf(AirportNotFoundException.class)
                    .hasMessage("Airport not found with id: 999");

            verify(airportRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("findByIataCode Tests")
    class FindByIataCodeTests {

        @Test
        @DisplayName("should return airport by IATA code")
        void shouldReturnAirportByIataCode() {
            when(airportRepository.findByIataCode("MAD")).thenReturn(Optional.of(airport));

            var result = airportService.findByIataCode("MAD");

            assertThat(result).isNotNull();
            assertThat(result.getIataCode()).isEqualTo("MAD");
            verify(airportRepository, times(1)).findByIataCode("MAD");
        }

        @Test
        @DisplayName("should throw exception when airport not found by IATA code")
        void shouldThrowExceptionWhenAirportNotFoundByIataCode() {
            when(airportRepository.findByIataCode("INVALID")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> airportService.findByIataCode("INVALID"))
                    .isInstanceOf(AirportNotFoundException.class)
                    .hasMessage("Airport not found with Iata code: INVALID");

            verify(airportRepository, times(1)).findByIataCode("INVALID");
        }
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all airports")
        void shouldReturnAllAirports() {
            var airport2 = new Airport(2L, "BCN", "Barcelona-El Prat", "Barcelona", "Spain");
            List<Airport> airports = Arrays.asList(airport, airport2);
            when(airportRepository.findAll()).thenReturn(airports);

            var result = airportService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result).contains(airport, airport2);
            verify(airportRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("should return empty list when no airports")
        void shouldReturnEmptyListWhenNoAirports() {
            when(airportRepository.findAll()).thenReturn(Arrays.asList());

            var result = airportService.findAll();

            assertThat(result).isEmpty();
            verify(airportRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("findAllByCity Tests")
    class FindAllByCityTests {

        @Test
        @DisplayName("should return airports by city")
        void shouldReturnAirportsByCity() {
            List<Airport> airports = Arrays.asList(airport);
            when(airportRepository.findAllByCity("Madrid")).thenReturn(airports);

            var result = airportService.findAllByCity("Madrid");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCity()).isEqualTo("Madrid");
            verify(airportRepository, times(1)).findAllByCity("Madrid");
        }
    }

    @Nested
    @DisplayName("findAllByCountry Tests")
    class FindAllByCountryTests {

        @Test
        @DisplayName("should return airports by country")
        void shouldReturnAirportsByCountry() {
            List<Airport> airports = Arrays.asList(airport);
            when(airportRepository.findAllByCountry("Spain")).thenReturn(airports);

            var result = airportService.findAllByCountry("Spain");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCountry()).isEqualTo("Spain");
            verify(airportRepository, times(1)).findAllByCountry("Spain");
        }
    }

    @Nested
    @DisplayName("createAirport Tests")
    class CreateAirportTests {

        @Test
        @DisplayName("should create new airport")
        void shouldCreateNewAirport() {
            when(airportRepository.save(airport)).thenReturn(airport);

            var result = airportService.createAirport(airport);

            assertThat(result).isNotNull();
            assertThat(result.getIataCode()).isEqualTo("MAD");
            verify(airportRepository, times(1)).save(airport);
        }
    }

    @Nested
    @DisplayName("updateAirport Tests")
    class UpdateAirportTests {

        @Test
        @DisplayName("should update existing airport")
        void shouldUpdateExistingAirport() {
            var updatedAirport = new Airport(1L, "MAD", "Updated Name", "Madrid", "Spain");

            when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));
            when(airportRepository.save(any(Airport.class))).thenReturn(updatedAirport);

            var result = airportService.updateAirport(1L, updatedAirport);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Updated Name");
            verify(airportRepository, times(1)).findById(1L);
            verify(airportRepository, times(1)).save(any(Airport.class));
        }

        @Test
        @DisplayName("should throw exception when airport not found for update")
        void shouldThrowExceptionWhenAirportNotFoundForUpdate() {
            when(airportRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> airportService.updateAirport(999L, airport))
                    .isInstanceOf(AirportNotFoundException.class);

            verify(airportRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("deleteAirportById Tests")
    class DeleteAirportByIdTests {

        @Test
        @DisplayName("should delete existing airport")
        void shouldDeleteExistingAirport() {
            when(airportRepository.findById(1L)).thenReturn(Optional.of(airport));

            airportService.deleteAirportById(1L);

            verify(airportRepository, times(1)).findById(1L);
            verify(airportRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("should throw exception when airport not found for deletion")
        void shouldThrowExceptionWhenAirportNotFoundForDeletion() {
            when(airportRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> airportService.deleteAirportById(999L))
                    .isInstanceOf(AirportNotFoundException.class);

            verify(airportRepository, times(1)).findById(999L);
            verify(airportRepository, never()).deleteById(999L);
        }
    }
}
