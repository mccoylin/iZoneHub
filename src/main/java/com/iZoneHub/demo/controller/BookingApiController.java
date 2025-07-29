package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.dto.BookingRequest;
import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.model.User; // 確保已引入
import com.iZoneHub.demo.security.CustomUserDetails;
import com.iZoneHub.demo.service.BookingService;
import jakarta.servlet.http.HttpSession; // 確保已引入
import org.springframework.http.HttpStatus; // 確保已引入
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
// ... 其他 import ...

@RestController
@RequestMapping("/api/bookings")
public class BookingApiController {

    private final BookingService bookingService;

    public BookingApiController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/unavailable")
    public ResponseEntity<List<Integer>> getUnavailableHours(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(bookingService.getUnavailableHours(roomId, date));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "尚未登入，請先登入後再預約"));
        }

        try {
            Booking newBooking = bookingService.createBooking(request, userDetails.getId());
            return ResponseEntity.ok(Map.of("message", "預約成功！", "bookingId", newBooking.getId()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "伺服器發生未預期的錯誤，請稍後再試。"));
        }
    }
}
