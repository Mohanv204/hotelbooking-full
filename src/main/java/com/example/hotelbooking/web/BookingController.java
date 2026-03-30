package com.example.hotelbooking.web;

import com.example.hotelbooking.dto.BookingDtos;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDtos.BookingDto create(@Valid @RequestBody BookingDtos.CreateBookingRequest request) {
        Long userId = currentUserId();
        Booking booking = bookingService.createBooking(userId, request);
        return toDto(booking);
    }

    @PatchMapping("/{bookingId}/cancel")
    public BookingDtos.BookingDto cancel(@PathVariable Long bookingId) {
        Long userId = currentUserId();
        Booking booking = bookingService.cancelBooking(userId, bookingId);
        return toDto(booking);
    }

    @GetMapping("/me")
    public List<BookingDtos.BookingDto> myBookings() {
        Long userId = currentUserId();
        return bookingService
                .listBookingsForUser(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new org.springframework.web.server.ResponseStatusException(UNAUTHORIZED, "Unauthorized");
        }
        return (Long) auth.getPrincipal();
    }

    private BookingDtos.BookingDto toDto(Booking booking) {
        return new BookingDtos.BookingDto(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getStatus().name()
        );
    }
}

