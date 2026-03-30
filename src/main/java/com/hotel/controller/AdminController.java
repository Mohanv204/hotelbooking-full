package com.hotel.controller;

import com.hotel.dto.*;
import com.hotel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final HotelService hotelService;
    private final BookingService bookingService;

    // ── Dashboard ─────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> dashboard() {
        return ResponseEntity.ok(bookingService.getDashboard());
    }

    // ── Hotels ────────────────────────────────────────────────────────────
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> allHotels() {
        return ResponseEntity.ok(hotelService.getAllHotelsAdmin());
    }

    @PostMapping("/hotels")
    public ResponseEntity<HotelDto> createHotel(@Valid @RequestBody CreateHotelRequest req) {
        return ResponseEntity.ok(hotelService.createHotel(req));
    }

    @PutMapping("/hotels/{id}")
    public ResponseEntity<HotelDto> updateHotel(
            @PathVariable Long id, @Valid @RequestBody CreateHotelRequest req) {
        return ResponseEntity.ok(hotelService.updateHotel(id, req));
    }

    // ── Rooms ─────────────────────────────────────────────────────────────
    @PostMapping("/rooms")
    public ResponseEntity<RoomDto> addRoom(@Valid @RequestBody CreateRoomRequest req) {
        return ResponseEntity.ok(hotelService.addRoom(req));
    }

    // ── Bookings ──────────────────────────────────────────────────────────
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> allBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<BookingDto> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }
}
