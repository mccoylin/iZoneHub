package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.dto.BookingRequest;
import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings") // 所有 API 都以 /api/bookings 開頭
public class BookingApiController {

    private final BookingService bookingService;

    public BookingApiController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * 獲取指定房間在特定日期的「不可預約」小時列表
     * 前端請求範例: /api/bookings/5/unavailable-slots?date=2024-08-15
     */
    @GetMapping("/{roomId}/unavailable-slots")
    public ResponseEntity<List<Integer>> getUnavailableSlots(
            @PathVariable Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Integer> unavailableHours = bookingService.getUnavailableHours(roomId, date);
        return ResponseEntity.ok(unavailableHours);
    }

    /**
     * 建立一筆新的預約
     * 前端會傳送一個包含預約資訊的 JSON 物件
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            Booking newBooking = bookingService.createBooking(bookingRequest);
            // 回傳成功訊息和新建立的 booking ID
            return ResponseEntity.ok(Map.of("message", "預約成功！", "bookingId", newBooking.getId()));
        } catch (IllegalStateException e) {
            // 如果時段已被預約或時間無效，回傳錯誤訊息
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

// 建議建立一個 DTO (Data Transfer Object) 來接收請求
// public record BookingRequest(Long roomId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {}