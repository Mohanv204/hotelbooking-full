package com.hotelbooking.app.hotelbooking.controller;

import com.hotelbooking.app.hotelbooking.dto.BookingRequest;
import com.hotelbooking.app.hotelbooking.entity.Booking;
import com.hotelbooking.app.hotelbooking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
// REMOVED: @RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    // ADDED: Manual constructor for Dependency Injection
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /api/bookings
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    // GET /api/bookings/user/{id}
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(id));
    }

    // DELETE /api/bookings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}