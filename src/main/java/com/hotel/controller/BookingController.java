package com.hotel.controller;

import com.hotel.dto.*;
import com.hotel.entity.User;
import com.hotel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateBookingRequest req) {
        return ResponseEntity.ok(bookingService.createBooking(user.getId(), req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> myBookings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingService.getMyBookings(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, user.getId()));
    }
}
