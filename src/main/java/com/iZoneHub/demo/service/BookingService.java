package com.iZoneHub.demo.service;

import com.iZoneHub.demo.dto.BookingRequest;
import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.repository.BookingRepository;
import com.iZoneHub.demo.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    // private final UserRepository userRepository; // 未來擴充使用者功能時需要

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * 獲取指定房間在特定日期的「不可預約」小時列表。
     *
     * @param roomId 房間 ID
     * @param date   查詢的日期
     * @return 一個包含不可預約小時 (0-23) 的整數列表
     */
    @Transactional(readOnly = true)
    public List<Integer> getUnavailableHours(Long roomId, LocalDate date) {
        // 1. 定義查詢日期的 UTC 時間範圍
        ZoneId systemZone = ZoneId.systemDefault();
        Instant startOfDayUTC = date.atStartOfDay(systemZone).toInstant();
        Instant endOfDayUTC = date.plusDays(1).atStartOfDay(systemZone).toInstant();

        // 2. 使用新的、更穩健的查詢，找出所有與當天有重疊的預約
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomId, startOfDayUTC, endOfDayUTC);

// 3. 為每個重疊的預約，精確計算其在「當天」所佔用的小時
        return overlappingBookings.stream()
                .flatMap(booking -> {
                    ZonedDateTime dayStart = startOfDayUTC.atZone(systemZone);
                    ZonedDateTime dayEnd = endOfDayUTC.atZone(systemZone);

                    // 找出預約在「當天」的有效開始時間 (取預約開始時間和當天0點的較晚者)
                    ZonedDateTime effectiveStart = booking.getStartTime().atZone(systemZone).isBefore(dayStart)
                            ? dayStart
                            : booking.getStartTime().atZone(systemZone);

                    // 找出預約在「當天」的有效結束時間 (取預約結束時間和當天24點的較早者)
                    ZonedDateTime effectiveEnd = booking.getEndTime().atZone(systemZone).isAfter(dayEnd)
                            ? dayEnd
                            : booking.getEndTime().atZone(systemZone);

                    int startHour = effectiveStart.getHour();
                    int endHour = effectiveEnd.getHour();

                    // IntStream.range 是不包含結束值的，這正好符合我們的需求
                    // 例如，14:00 到 16:00 的預約，會產生 14 和 15 這兩個小時
                    return IntStream.range(startHour, endHour).boxed();
                })
                .distinct() // 去除重複的小時
                .collect(Collectors.toList());
    }

    /**
     * 建立一筆新的預約，並執行所有必要的驗證。
     *
     * @param request 包含預約資訊的 DTO
     * @return 成功儲存到資料庫的 Booking 物件
     * @throws IllegalStateException 如果驗證失敗（例如時間無效、時段衝突）
     */
    @Transactional
    public Booking createBooking(BookingRequest request) {

            // 1. 驗證輸入時間
            // [修正] 使用 !isBefore() 來替代 isAfter() || isEqual()，程式碼更簡潔且意圖更清晰。
            // 這段程式碼的語意是：「如果開始時間沒有在結束時間之前，則拋出例外」。
            if (!request.startTime().isBefore(request.endTime())) {
                throw new IllegalStateException("結束時間必須晚於開始時間。");
            }
            // [修正] 使用 Instant.now() 進行比較，確保時間比較的準確性
            if (request.startTime().isBefore(Instant.now())) {
                throw new IllegalStateException("無法預約過去的時間。");
            }

            // 2. 檢查時段是否已被預約 (核心邏輯)
            boolean isOverlapping = bookingRepository.existsOverlappingBooking(
                    request.roomId(), request.startTime(), request.endTime());
            if (isOverlapping) {
                throw new IllegalStateException("該時段已被預約，請選擇其他時間。");
            }


// 3. 獲取相關實體 (房間、使用者)
        Rooms room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new IllegalStateException("找不到 ID 為 " + request.roomId() + " 的房間。"));

        // User user = userRepository.findById(request.userId())
        //         .orElseThrow(() -> new IllegalStateException("找不到使用者。"));

        // 4. 計算價格
        long hours = Duration.between(request.startTime(), request.endTime()).toHours();
        BigDecimal totalPrice = room.getPrice().multiply(new BigDecimal(hours));

        // 5. 建立並儲存 Booking 實體
        Booking newBooking = new Booking();
        newBooking.setRoom(room);
        // newBooking.setUser(user);
        newBooking.setStartTime(request.startTime());
        newBooking.setEndTime(request.endTime());
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus("CONFIRMED");

        return bookingRepository.save(newBooking);
    }
}