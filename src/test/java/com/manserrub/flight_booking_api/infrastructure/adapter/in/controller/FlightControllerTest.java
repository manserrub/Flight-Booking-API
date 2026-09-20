package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manserrub.flight_booking_api.application.service.FlightService;
import com.manserrub.flight_booking_api.domain.exception.FlightNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.FlightRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.FlightWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
@DisplayName("Flight Controller Tests")
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;

    @MockBean
    private FlightWebMapper webMapper;

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
    @DisplayName("GET /api/v1/flights Tests")
    class ListAllTests {

        @Test
        @DisplayName("should return all flights")
        void shouldReturnAllFlights() throws Exception {
            List<Flight> flights = Arrays.asList(flight);
            when(flightService.execute()).thenReturn(flights);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/flights"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(flightService, times(1)).execute();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/flights/{id} Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return flight when found")
        void shouldReturnFlightWhenFound() throws Exception {
            when(flightService.execute(1L)).thenReturn(flight);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/flights/1"))
                    .andExpect(status().isOk());

            verify(flightService, times(1)).execute(1L);
        }

        @Test
        @DisplayName("should return 404 when flight not found")
        void shouldReturn404WhenFlightNotFound() throws Exception {
            when(flightService.execute(999L)).thenThrow(new FlightNotFoundException("Flight not found"));

            mockMvc.perform(get("/api/v1/flights/999"))
                    .andExpect(status().isNotFound());

            verify(flightService, times(1)).execute(999L);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/flights Tests")
    class CreateTests {

        @Test
        @DisplayName("should create new flight")
        void shouldCreateNewFlight() throws Exception {
            var request = new FlightRequest();
            when(webMapper.toDomain(any(FlightRequest.class))).thenReturn(flight);
            when(flightService.create(any(Flight.class))).thenReturn(flight);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(post("/api/v1/flights")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(flightService, times(1)).create(any(Flight.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/flights/{id} Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update existing flight")
        void shouldUpdateExistingFlight() throws Exception {
            var request = new FlightRequest();
            when(webMapper.toDomain(eq(1L), any(FlightRequest.class))).thenReturn(flight);
            when(flightService.update(eq(1L), any(Flight.class))).thenReturn(flight);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(put("/api/v1/flights/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(flightService, times(1)).update(eq(1L), any(Flight.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/flights/{id} Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete flight")
        void shouldDeleteFlight() throws Exception {
            mockMvc.perform(delete("/api/v1/flights/1"))
                    .andExpect(status().isNoContent());

            verify(flightService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("should return 404 when deleting non-existent flight")
        void shouldReturn404WhenDeletingNonExistentFlight() throws Exception {
            doThrow(new FlightNotFoundException("Flight not found")).when(flightService).delete(999L);

            mockMvc.perform(delete("/api/v1/flights/999"))
                    .andExpect(status().isNotFound());

            verify(flightService, times(1)).delete(999L);
        }
    }
}
