package com.iZoneHub.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.model.Rooms.RoomType;
import com.iZoneHub.demo.repository.RoomRepository;

/**
 * 房間相關的服務層。
 * 負責處理房間查詢等商業邏輯。
 */
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Retrieves all rooms from the repository.
     *
     * @return a list of all rooms
     */
    public List<Rooms> getAllRooms()
    {
        return roomRepository.findAll();
    }

    /**
     * Retrieves rooms by their category.
     *
     * @param roomType the category of the rooms to retrieve
     * @return a list of rooms that match the specified category
     */
    public List<Rooms> getRoomsByCategory (RoomType roomType)
    {
        return roomRepository.findByRoomType(roomType);
    }

    /**
     * Retrieves a room by its ID.
     *
     * @param id the ID of the room to retrieve
     * @return the room with the specified ID, or null if not found
     */
    public Rooms getRoomById(Long id)
    {
        return roomRepository.findById(id).orElse(null);
    }

    /**
     * (建議新增) 以 Optional 的形式根據 ID 查找房間，這是更現代且安全的作法。
     *
     * @param id the ID of the room to retrieve
     * @return an Optional containing the room if found, or an empty Optional if not
     */
    public Optional<Rooms> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

}
