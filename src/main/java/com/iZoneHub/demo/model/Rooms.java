package com.iZoneHub.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


import java.math.BigDecimal;

@Data                   // Data：自動生成所有字段的 getter、setter 方法，以及 toString()、equals() 和 hashCode() 方法。
// @Builder                // Builder：為 class 生成建造者模式（Builder Pattern）的支持，允許通過流式 API 創建對象。
@AllArgsConstructor     // AllArgsConstructor：生成包含所有字段的參數化構造函數。
@NoArgsConstructor      // @NoArgsConstructor：生成無參構造函數。
@Entity
@Table(name = "rooms")
public class Rooms
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;        // Room name

    private String description;     // Room description

    @Column(nullable = false)
    private BigDecimal price;       // Room price

    @Column(name = "image_path")
    private String imagePath;       // Path to room image

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;      // 類型 (normal, KTV, projection)

    // Enum for room types
    public enum RoomType
    {
        normal, KTV, projection
    }

}
