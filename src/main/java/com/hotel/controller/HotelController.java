package com.hotel.controller;

import com.hotel.dto.*;
import com.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchHotels(@RequestParam String location) {
        return ResponseEntity.ok(hotelService.searchHotels(location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<RoomDto>> getRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getRoomsByHotel(hotelId));
    }

    @GetMapping("/{hotelId}/rooms/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms(
            @PathVariable Long hotelId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return ResponseEntity.ok(hotelService.getAvailableRooms(hotelId, checkIn, checkOut));
    }
}
