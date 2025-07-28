package com.iZoneHub.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [核心修復] 建立 Booking 到 User 的多對一關聯
    @ManyToOne(fetch = FetchType.LAZY) // 一個使用者可以有多個預約
    @JoinColumn(name = "user_id", nullable = false) // 對應到資料庫中的 user_id 欄位，且不允許為空
    private User user;

    // [核心修復] 建立 Booking 到 Rooms 的多對一關聯
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false) // 對應到資料庫中的 room_id 欄位
    private Rooms room;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    private String status;

    private Instant createdAt = Instant.now();
}