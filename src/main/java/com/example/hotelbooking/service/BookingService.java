package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.BookingDtos;
import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.BookingStatus;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.BookingRepository;
import com.example.hotelbooking.repository.RoomRepository;
import com.example.hotelbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking createBooking(Long userId, BookingDtos.CreateBookingRequest request) {
        if (request.checkOut() == null || request.checkIn() == null || !request.checkOut().isAfter(request.checkIn())) {
            throw new ResponseStatusException(BAD_REQUEST, "checkOut must be after checkIn.");
        }

        Room room = roomRepository.findByIdForUpdate(request.roomId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Room not found."));

        boolean unavailable = bookingRepository.existsOverlapping(
                room.getId(),
                request.checkIn(),
                request.checkOut(),
                BookingStatus.CONFIRMED
        );

        if (unavailable) {
            throw new ResponseStatusException(BAD_REQUEST, "The room is no longer available for the selected dates.");
        }

        User userRef = userRepository.getReferenceById(userId);

        Booking booking = new Booking();
        booking.setUser(userRef);
        booking.setRoom(room);
        booking.setCheckIn(request.checkIn());
        booking.setCheckOut(request.checkOut());
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Booking not found."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(BAD_REQUEST, "Booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> listBookingsForUser(Long userId) {
        return bookingRepository.findAllByUser_Id(userId);
    }
}

