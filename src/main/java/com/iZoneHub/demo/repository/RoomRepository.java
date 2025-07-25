package com.iZoneHub.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.iZoneHub.demo.model.Rooms;

public interface RoomRepository extends JpaRepository<Rooms, Long>
{
    List<Rooms> findByRoomType(Rooms.RoomType roomType);

}
