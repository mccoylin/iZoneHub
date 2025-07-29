package com.iZoneHub.demo.service;

import com.iZoneHub.demo.dto.BookingRequest;
import com.iZoneHub.demo.model.Booking;
import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.model.User;
import com.iZoneHub.demo.repository.BookingRepository;
import com.iZoneHub.demo.repository.RoomRepository;
import com.iZoneHub.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookingService {

    // [偵錯] 加入 Logger
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Integer> getUnavailableHours(Long roomId, LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime startOfDay = date.atStartOfDay(zoneId);
        ZonedDateTime endOfDay = startOfDay.plusDays(1);

        List<Booking> bookings = bookingRepository.findOverlappingBookings(
                roomId,
                startOfDay.toInstant(),
                endOfDay.toInstant());

        return bookings.stream()
                .flatMap(b -> {
                    ZonedDateTime bookingStart = b.getStartTime().atZone(zoneId);
                    ZonedDateTime bookingEnd = b.getEndTime().atZone(zoneId);

                    ZonedDateTime start = bookingStart.isBefore(startOfDay) ? startOfDay : bookingStart;
                    ZonedDateTime end = bookingEnd.isAfter(endOfDay) ? endOfDay : bookingEnd;

                    return IntStream.iterate(start.getHour(), h -> h + 1)
                            .limit((int) Duration.between(start.truncatedTo(ChronoUnit.HOURS), end).toHours())
                            .boxed();
                })
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Transactional
    public Booking createBooking(BookingRequest request, Long userId) {
        log.info("===== [Service] 進入 createBooking，接收到 userId: {} =====", userId);

        if (userId == null) {
            log.error("[Service] 嚴重錯誤：傳入的 userId 為 NULL。");
            throw new IllegalArgumentException("User ID 不能為 null。");
        }

        // ... 時間驗證和時段衝突檢查 ...
        if (!request.startTime().isBefore(request.endTime())) {
            throw new IllegalStateException("結束時間必須晚於開始時間。");
        }
        if (request.startTime().isBefore(Instant.now())) {
            throw new IllegalStateException("無法預約過去的時間。");
        }
        boolean isOverlapping = bookingRepository.existsOverlappingBooking(
                request.roomId(), request.startTime(), request.endTime());
        if (isOverlapping) {
            throw new IllegalStateException("該時段已被預約，請選擇其他時間。");
        }

        log.info("[Service] 正在查找 Room，ID: {}", request.roomId());
        Rooms room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> {
                    log.warn("[Service] 查找 Room 失敗，ID: {}", request.roomId());
                    return new IllegalStateException("找不到 ID 為 " + request.roomId() + " 的房間。");
                });
        log.info("[Service] 成功找到 Room: {}", room.getName());

        log.info("[Service] 正在查找 User，ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("[Service] 查找 User 失敗，ID: {}", userId);
                    return new IllegalStateException("找不到 ID 為 " + userId + " 的使用者資訊，您的登入可能已失效。");
                });
        log.info("[Service] 成功找到 User: {}", user.getName());

        // ... 價格計算 ...
        Duration duration = Duration.between(request.startTime(), request.endTime());
        long minutes = duration.toMinutes();
        long hours = (long) Math.ceil((double) minutes / 60.0);
        if (hours == 0 && minutes > 0) {
            hours = 1;
        }
        BigDecimal totalPrice = room.getPrice().multiply(new BigDecimal(hours));

        Booking newBooking = new Booking();
        newBooking.setRoom(room);
        newBooking.setUser(user);
        newBooking.setStartTime(request.startTime());
        newBooking.setEndTime(request.endTime());
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus("CONFIRMED");

        log.info("[Service] 準備將 Booking 物件存入資料庫...");
        Booking savedBooking = bookingRepository.save(newBooking);
        log.info("[Service] Booking 成功存入資料庫，新的 ID: {}", savedBooking.getId());
        return savedBooking;
    }
}
