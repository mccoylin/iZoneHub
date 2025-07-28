package com.iZoneHub.demo.repository;

import com.iZoneHub.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // [移除] 舊的查詢方法不夠穩健，將被新的 findOverlappingBookings 取代
    // List<Booking> findByRoomIdAndStartTimeBetween(Long roomId, Instant start, Instant end);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.startTime < :endOfDay AND b.endTime > :startOfDay")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("startOfDay") Instant startOfDay,
                                          @Param("endOfDay") Instant endOfDay);
    /**
     * 檢查指定房間在請求的時間範圍內是否存在任何重疊的預約。
     * 這是防止重複預約的核心查詢。
     * 如果 (現有預約的開始時間 < 請求的結束時間) 且 (現有預約的結束時間 > 請求的開始時間)，則代表重疊。
     *
     * @param roomId    房間 ID
     * @param startTime 請求的開始時間
     * @param endTime   請求的結束時間
     * @return 如果有重疊，則返回 true；否則返回 false
     */
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b " +
            "WHERE b.room.id = :roomId " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("startTime") Instant startTime,
                                     @Param("endTime") Instant endTime);
}