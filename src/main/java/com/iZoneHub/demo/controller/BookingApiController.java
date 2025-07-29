package com.iZoneHub.demo.controller;

import com.iZoneHub.demo.dto.BookingRequest;
import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.model.User; // 確保已引入
import com.iZoneHub.demo.service.BookingService;
import jakarta.servlet.http.HttpSession; // 確保已引入
import org.springframework.http.HttpStatus; // 確保已引入
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request, HttpSession session) {
        // [解決方案] 在這裡加入一個強大的防衛敘述
        User currentUser = (User) session.getAttribute("loggedInUser");

        // 1. 檢查 Session 中是否存在使用者。
        if (currentUser == null) {
            // 如果不存在，代表使用者未正確認證。拒絕此請求。
            // 這是防止資料庫錯誤最重要的一步。
            return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403 Forbidden 是最適合的狀態碼
                    .body(Map.of("error", "您的登入已過期或無效，請重新登入後再試。"));
        }

        // 2. 如果檢查通過，我們就能保證有一個有效的使用者 ID。
        try {
            // 將有效的使用者 ID 傳遞給 Service 層。
            Booking newBooking = bookingService.createBooking(request, currentUser.getId());
            return ResponseEntity.ok(Map.of("message", "預約成功！", "bookingId", newBooking.getId()));
        } catch (IllegalStateException e) {
            // 現在這裡只會捕捉到業務邏輯錯誤（例如，時段已被預約）
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // 捕捉任何其他未預期的伺服器錯誤
            // 建議在此處加入日誌紀錄以供除錯
            // log.error("為使用者 {} 建立預約時發生錯誤", currentUser.getId(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "伺服器發生未預期的錯誤，請稍後再試。"));
        }
    }
}
