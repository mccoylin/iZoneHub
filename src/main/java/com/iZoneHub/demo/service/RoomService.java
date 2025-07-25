package com.iZoneHub.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.iZoneHub.demo.model.Rooms;
import com.iZoneHub.demo.model.Rooms.RoomType;
import com.iZoneHub.demo.repository.RoomRepository;

@Service
public class RoomService
{
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository)
    {
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

}
