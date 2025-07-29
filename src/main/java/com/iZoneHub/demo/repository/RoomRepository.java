package com.iZoneHub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.iZoneHub.demo.model.Rooms;

/**
 * 房間資料庫存取介面。
 * 提供以房間類型查詢的功能。
 */
public interface RoomRepository extends JpaRepository<Rooms, Long> {
    List<Rooms> findByRoomType(Rooms.RoomType roomType);

}
