package com.hotelbooking.app.hotelbooking.controller;

import com.hotelbooking.app.hotelbooking.entity.Room;
import com.hotelbooking.app.hotelbooking.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
// REMOVED: @RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    private final RoomService roomService;

    // ADDED: Manual Constructor
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // GET /api/rooms/hotel/{hotelId}
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    // GET /api/rooms/hotel/{hotelId}/available
    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAvailableRooms(hotelId));
    }

    // POST /api/rooms/hotel/{hotelId}
    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<Room> addRoom(@PathVariable Long hotelId,
                                         @RequestBody Room room) {
        return ResponseEntity.ok(roomService.addRoom(hotelId, room));
    }

    // DELETE /api/rooms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }
}