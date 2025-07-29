package com.iZoneHub.demo.service;

import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.repository.BookingRepository;
import com.iZoneHub.demo.repository.RoomRepository;
import com.iZoneHub.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    UserRepository userRepository;

    BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService(bookingRepository, roomRepository, userRepository);
    }

    @Test
    void getUnavailableHoursReturnsSortedHours() {
        LocalDate date = LocalDate.of(2024, 5, 1);
        ZoneId zone = ZoneId.systemDefault();
        Booking b1 = new Booking();
        b1.setStartTime(date.atTime(13,30).atZone(zone).toInstant());
        b1.setEndTime(date.atTime(15,0).atZone(zone).toInstant());
        Booking b2 = new Booking();
        b2.setStartTime(date.atTime(9,0).atZone(zone).toInstant());
        b2.setEndTime(date.atTime(10,0).atZone(zone).toInstant());

        when(bookingRepository.findOverlappingBookings(eq(1L), any(), any()))
                .thenReturn(List.of(b1, b2));

        List<Integer> hours = bookingService.getUnavailableHours(1L, date);
        assertEquals(List.of(9,13,14), hours);
    }
}

