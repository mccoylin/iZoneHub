package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingApiController.class)
@Import(BookingApiControllerTest.TestConfig.class)
public class BookingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    @Configuration
    static class TestConfig {
        @Bean
        public BookingService bookingService() {
            return Mockito.mock(BookingService.class);
        }
    }

    @Test
    void getUnavailableHoursEndpointReturnsHours() throws Exception {
        when(bookingService.getUnavailableHours(1L, LocalDate.parse("2024-05-01")))
                .thenReturn(List.of(9, 10));

        mockMvc.perform(get("/api/bookings/unavailable")
                        .param("roomId", "1")
                        .param("date", "2024-05-01"))
                .andExpect(status().isOk())
                .andExpect(content().json("[9,10]"));
    }
}

