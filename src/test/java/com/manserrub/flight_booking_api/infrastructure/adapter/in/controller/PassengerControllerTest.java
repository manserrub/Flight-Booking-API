package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manserrub.flight_booking_api.application.service.PassengerService;
import com.manserrub.flight_booking_api.domain.exception.PassengerNotFoundException;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.PassengerRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.PassengerWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PassengerController.class)
@DisplayName("Passenger Controller Tests")
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerService passengerService;

    @MockBean
    private PassengerWebMapper webMapper;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
    }

    @Nested
    @DisplayName("GET /api/v1/passengers Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all passengers")
        void shouldReturnAllPassengers() throws Exception {
            List<Passenger> passengers = Arrays.asList(passenger);
            when(passengerService.findAll()).thenReturn(passengers);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/passengers"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(passengerService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/passengers/{id} Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return passenger when found")
        void shouldReturnPassengerWhenFound() throws Exception {
            when(passengerService.execute(1L)).thenReturn(passenger);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/passengers/1"))
                    .andExpect(status().isOk());

            verify(passengerService, times(1)).execute(1L);
        }

        @Test
        @DisplayName("should return 404 when passenger not found")
        void shouldReturn404WhenPassengerNotFound() throws Exception {
            when(passengerService.execute(999L)).thenThrow(new PassengerNotFoundException("Passenger not found"));

            mockMvc.perform(get("/api/v1/passengers/999"))
                    .andExpect(status().isNotFound());

            verify(passengerService, times(1)).execute(999L);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/passengers Tests")
    class CreateTests {

        @Test
        @DisplayName("should create new passenger")
        void shouldCreateNewPassenger() throws Exception {
            var request = new PassengerRequest();
            when(webMapper.toDomain(any(PassengerRequest.class))).thenReturn(passenger);
            when(passengerService.execute(any(Passenger.class))).thenReturn(passenger);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(post("/api/v1/passengers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(passengerService, times(1)).execute(any(Passenger.class));
        }

        @Test
        @DisplayName("should return 400 for invalid email")
        void shouldReturn400ForInvalidEmail() throws Exception {
            var invalidPassenger = new Passenger(1L, "John", "Doe", "invalid-email", "A123456",
                    LocalDate.now().minusYears(30));
            var request = new PassengerRequest();

            when(webMapper.toDomain(any(PassengerRequest.class))).thenReturn(invalidPassenger);
            when(passengerService.execute(any(Passenger.class)))
                    .thenThrow(new IllegalArgumentException("Invalid email format"));

            mockMvc.perform(post("/api/v1/passengers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/passengers/{id} Tests")
    class UpdateTests {

        @Test
        @DisplayName("should update existing passenger")
        void shouldUpdateExistingPassenger() throws Exception {
            var request = new PassengerRequest();
            when(webMapper.toDomain(eq(1L), any(PassengerRequest.class))).thenReturn(passenger);
            when(passengerService.execute(eq(1L), any(Passenger.class))).thenReturn(passenger);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(put("/api/v1/passengers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(passengerService, times(1)).execute(eq(1L), any(Passenger.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/passengers/{id} Tests")
    class DeleteTests {

        @Test
        @DisplayName("should delete passenger")
        void shouldDeletePassenger() throws Exception {
            mockMvc.perform(delete("/api/v1/passengers/1"))
                    .andExpect(status().isNoContent());

            verify(passengerService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("should return 404 when deleting non-existent passenger")
        void shouldReturn404WhenDeletingNonExistentPassenger() throws Exception {
            doThrow(new PassengerNotFoundException("Passenger not found")).when(passengerService).delete(999L);

            mockMvc.perform(delete("/api/v1/passengers/999"))
                    .andExpect(status().isNotFound());

            verify(passengerService, times(1)).delete(999L);
        }
    }
}
