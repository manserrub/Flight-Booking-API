package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manserrub.flight_booking_api.application.service.BookingService;
import com.manserrub.flight_booking_api.domain.exception.BookingNotFoundException;
import com.manserrub.flight_booking_api.domain.exception.SeatNotAvailableException;
import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.model.Booking;
import com.manserrub.flight_booking_api.domain.model.Flight;
import com.manserrub.flight_booking_api.domain.model.Passenger;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.BookingRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.BookingWebMapper;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@DisplayName("Booking Controller Tests")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingWebMapper webMapper;

    private Airport departureAirport;
    private Airport arrivalAirport;
    private Flight flight;
    private Passenger passenger;
    private Booking booking;

    @BeforeEach
    void setUp() {
        departureAirport = new Airport(1L, "MAD", "Madrid", "Madrid", "Spain");
        arrivalAirport = new Airport(2L, "BCN", "Barcelona", "Barcelona", "Spain");
        flight = new Flight(1L, "AA123", departureAirport, arrivalAirport,
                LocalDateTime.of(2024, 12, 20, 10, 0),
                LocalDateTime.of(2024, 12, 20, 12, 0),
                150.0, 10, Flight.FlightStatus.SCHEDULED);
        passenger = new Passenger(1L, "John", "Doe", "john@example.com", "A123456",
                LocalDate.now().minusYears(30));
        booking = new Booking(flight, passenger, "12A");
    }

    @Nested
    @DisplayName("GET /api/v1/bookings Tests")
    class FindAllTests {

        @Test
        @DisplayName("should return all bookings")
        void shouldReturnAllBookings() throws Exception {
            List<Booking> bookings = Arrays.asList(booking);
            when(bookingService.findAll()).thenReturn(bookings);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/bookings"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            verify(bookingService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("GET /api/v1/bookings/{id} Tests")
    class FindByIdTests {

        @Test
        @DisplayName("should return booking when found")
        void shouldReturnBookingWhenFound() throws Exception {
            when(bookingService.findById(1L)).thenReturn(booking);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(get("/api/v1/bookings/1"))
                    .andExpect(status().isOk());

            verify(bookingService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("should return 404 when booking not found")
        void shouldReturn404WhenBookingNotFound() throws Exception {
            when(bookingService.findById(999L)).thenThrow(new BookingNotFoundException("Booking not found"));

            mockMvc.perform(get("/api/v1/bookings/999"))
                    .andExpect(status().isNotFound());

            verify(bookingService, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("POST /api/v1/bookings Tests")
    class CreateTests {

        @Test
        @DisplayName("should create new booking")
        void shouldCreateNewBooking() throws Exception {
            var request = new BookingRequest();
            when(webMapper.toDomain(any(BookingRequest.class))).thenReturn(booking);
            when(bookingService.execute(any(Booking.class))).thenReturn(booking);
            when(webMapper.toResponse(any())).thenReturn(null);

            mockMvc.perform(post("/api/v1/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(bookingService, times(1)).execute(any(Booking.class));
        }

        @Test
        @DisplayName("should return 400 when no seats are available")
        void shouldReturn400WhenNoSeatsAvailable() throws Exception {
            var request = new BookingRequest();
            when(webMapper.toDomain(any(BookingRequest.class))).thenReturn(booking);
            when(bookingService.execute(any(Booking.class)))
                    .thenThrow(new SeatNotAvailableException("No seats available"));

            mockMvc.perform(post("/api/v1/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(bookingService, times(1)).execute(any(Booking.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/bookings/{id} Tests")
    class CancelTests {

        @Test
        @DisplayName("should cancel booking")
        void shouldCancelBooking() throws Exception {
            mockMvc.perform(delete("/api/v1/bookings/1"))
                    .andExpect(status().isNoContent());

            verify(bookingService, times(1)).execute(1L);
        }

        @Test
        @DisplayName("should return 404 when booking not found for cancellation")
        void shouldReturn404WhenBookingNotFoundForCancellation() throws Exception {
            doThrow(new BookingNotFoundException("Booking not found")).when(bookingService).execute(999L);

            mockMvc.perform(delete("/api/v1/bookings/999"))
                    .andExpect(status().isNotFound());

            verify(bookingService, times(1)).execute(999L);
        }
    }
}
