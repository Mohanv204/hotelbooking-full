package com.hotelbooking.app.hotelbooking.service;

import com.hotelbooking.app.hotelbooking.entity.Hotel;
import com.hotelbooking.app.hotelbooking.entity.Room;
import com.hotelbooking.app.hotelbooking.repository.HotelRepository;
import com.hotelbooking.app.hotelbooking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// REMOVED: @RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    // ADDED: Manual constructor for injection
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    public List<Room> getAvailableRooms(Long hotelId) {
        return roomRepository.findByHotelIdAndAvailable(hotelId, true);
    }

    public Room addRoom(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));
        room.setHotel(hotel);
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    public String deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
        return "Room deleted successfully!";
    }
}