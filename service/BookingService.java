package com.hotelbooking.app.hotelbooking.service;

import com.hotelbooking.app.hotelbooking.dto.BookingRequest;
import com.hotelbooking.app.hotelbooking.entity.Booking;
import com.hotelbooking.app.hotelbooking.entity.Hotel;
import com.hotelbooking.app.hotelbooking.entity.Room;
import com.hotelbooking.app.hotelbooking.entity.User;
import com.hotelbooking.app.hotelbooking.repository.BookingRepository;
import com.hotelbooking.app.hotelbooking.repository.HotelRepository;
import com.hotelbooking.app.hotelbooking.repository.RoomRepository;
import com.hotelbooking.app.hotelbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// REMOVED: @RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService;

    // ADDED: Manual Constructor
    public BookingService(BookingRepository bookingRepository, 
                          UserRepository userRepository, 
                          HotelRepository hotelRepository, 
                          RoomRepository roomRepository, 
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.emailService = emailService;
    }

    public Booking createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found!"));

        // Mark room as unavailable
        room.setAvailable(false);
        roomRepository.save(room);

        // CHANGED: Replaced builder with standard instantiation
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setStatus("CONFIRMED");
        booking.setTotalPrice(request.getTotalPrice());

        Booking saved = bookingRepository.save(booking);

        emailService.sendBookingConfirmation(user.getEmail(), saved.getId(), hotel.getName());

        return saved;
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        booking.setStatus("CANCELLED");

        // Mark room as available again
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);

        bookingRepository.save(booking);

        emailService.sendBookingCancellation(booking.getUser().getEmail(), bookingId);

        return "Booking cancelled successfully!";
    }
}