package com.hotel.dto;
import com.hotel.entity.Booking;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long roomId;
    private String roomType;
    private Long hotelId;
    private String hotelName;
    private String hotelLocation;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guests;
    private Double totalPrice;
    private Booking.BookingStatus status;
    private LocalDateTime bookedAt;
    public static BookingDto from(Booking b) {
        return BookingDto.builder()
                .id(b.getId()).userId(b.getUser().getId())
                .userName(b.getUser().getName()).userEmail(b.getUser().getEmail())
                .roomId(b.getRoom().getId()).roomType(b.getRoom().getRoomType().name())
                .hotelId(b.getHotel().getId()).hotelName(b.getHotel().getName())
                .hotelLocation(b.getHotel().getLocation())
                .checkInDate(b.getCheckInDate()).checkOutDate(b.getCheckOutDate())
                .guests(b.getGuests()).totalPrice(b.getTotalPrice())
                .status(b.getStatus()).bookedAt(b.getBookedAt()).build();
    }
}
