package com.iZoneHub.demo.dto;

import java.time.Instant;

/**
 * 一個資料傳輸物件 (DTO)，用於封裝從前端傳來的新預約請求。
 * 使用 record 可以自動產生建構子、getter、equals、hashCode 和 toString 方法。
 *
 * @param roomId      預約的房間 ID
 * @param userId      預約的使用者 ID (未來擴充用)
 * @param startTime   預約開始時間
 * @param endTime     預約結束時間
 */
public record BookingRequest(
        Long roomId,
        Long userId, // 目前可以先不使用，但保留以供未來擴充
        Instant startTime,
        Instant endTime
) {}
