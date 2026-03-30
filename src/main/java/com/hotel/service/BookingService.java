package com.hotel.service;

import com.hotel.dto.*;
import com.hotel.entity.*;
import com.hotel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final RoomRepository roomRepo;
    private final HotelRepository hotelRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    @Transactional
    public BookingDto createBooking(Long userId, CreateBookingRequest req) {
        User user   = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Room room   = roomRepo.findById(req.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        Hotel hotel = hotelRepo.findById(req.getHotelId()).orElseThrow(() -> new RuntimeException("Hotel not found"));

        if (!room.getIsAvailable()) throw new RuntimeException("Room is not available");
        if (!req.getCheckInDate().isBefore(req.getCheckOutDate())) throw new RuntimeException("Check-out must be after check-in");
        if (req.getGuests() > room.getCapacity()) throw new RuntimeException("Guests exceed room capacity of " + room.getCapacity());

        long nights = ChronoUnit.DAYS.between(req.getCheckInDate(), req.getCheckOutDate());
        Booking booking = bookingRepo.save(Booking.builder()
                .user(user).room(room).hotel(hotel)
                .checkInDate(req.getCheckInDate()).checkOutDate(req.getCheckOutDate())
                .guests(req.getGuests()).totalPrice(nights * room.getPricePerNight())
                .status(Booking.BookingStatus.CONFIRMED).build());
        emailService.sendBookingConfirmation(booking);
        return BookingDto.from(booking);
    }

    @Transactional
    public BookingDto cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getUser().getId().equals(userId)) throw new RuntimeException("Not authorized");
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) throw new RuntimeException("Already cancelled");
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepo.save(booking);
        emailService.sendCancellationEmail(booking);
        return BookingDto.from(booking);
    }

    public List<BookingDto> getMyBookings(Long userId) {
        return bookingRepo.findByUserIdOrderByBookedAtDesc(userId).stream().map(BookingDto::from).collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id) {
        return BookingDto.from(bookingRepo.findById(id).orElseThrow(() -> new RuntimeException("Booking not found")));
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepo.findAllByOrderByBookedAtDesc().stream().map(BookingDto::from).collect(Collectors.toList());
    }

    @Transactional
    public BookingDto updateStatus(Long bookingId, String status) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.BookingStatus.valueOf(status));
        return BookingDto.from(bookingRepo.save(booking));
    }

    public DashboardDto getDashboard() {
        return DashboardDto.builder()
                .totalBookings(bookingRepo.count())
                .confirmedBookings(bookingRepo.countByStatus(Booking.BookingStatus.CONFIRMED))
                .cancelledBookings(bookingRepo.countByStatus(Booking.BookingStatus.CANCELLED))
                .completedBookings(bookingRepo.countByStatus(Booking.BookingStatus.COMPLETED))
                .totalHotels(hotelRepo.count())
                .totalUsers(userRepo.countByRole(User.Role.USER))
                .totalRevenue(bookingRepo.getTotalRevenue() != null ? bookingRepo.getTotalRevenue() : 0)
                .build();
    }
}
