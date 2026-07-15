package com.manserrub.flight_booking_api.infrastructure.adapter.in.controller;

import com.manserrub.flight_booking_api.domain.model.Airport;
import com.manserrub.flight_booking_api.domain.ports.in.FindAirportUseCase;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportRequest;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.dto.AirportResponse;
import com.manserrub.flight_booking_api.infrastructure.adapter.in.mapper.AirportWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AirportControllerTest {

    private MockMvc mvc;

    private FindAirportUseCase findAirportUseCase;
    private AirportWebMapper webMapper;

    @BeforeEach
    void setup() {
        findAirportUseCase = Mockito.mock(FindAirportUseCase.class);
        webMapper = Mockito.mock(AirportWebMapper.class);
        var controller = new AirportController(findAirportUseCase, webMapper);
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new com.manserrub.flight_booking_api.infrastructure.exception.GlobalExceptionHandler()).build();
    }

    @Test
    void createAirport_returnsCreatedAndLocation() throws Exception {
        var requestJson = "{\"iataCode\":\"ABC\",\"name\":\"Aeropuerto ABC\",\"city\":\"Ciudad\",\"country\":\"Pais\"}";

        var domain = new Airport(null, "ABC", "Aeropuerto ABC", "Ciudad", "Pais");
        var saved = new Airport(1L, "ABC", "Aeropuerto ABC", "Ciudad", "Pais");
        var response = new AirportResponse(1L, "ABC", "Aeropuerto ABC", "Ciudad", "Pais");

        when(webMapper.toDomain(any(AirportRequest.class))).thenReturn(domain);
        when(findAirportUseCase.createAirport(any(Airport.class))).thenReturn(saved);
        when(webMapper.toResponse(any(Airport.class))).thenReturn(response);

        mvc.perform(post("/api/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/airports/1")));
    }
}

