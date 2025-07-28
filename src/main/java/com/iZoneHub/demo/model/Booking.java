package com.iZoneHub.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter // 自動為所有欄位產生 getter 方法 (e.g., getRoom())
@Setter // 自動為所有欄位產生 setter 方法 (e.g., setRoom())
@NoArgsConstructor // 自動產生無參數的建構子
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private String status;

    // 所有手動編寫的 getter, setter 和建構子都可以刪除了！
}