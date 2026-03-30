package com.hotelbooking.app.hotelbooking.dto;

import java.time.LocalDate;

public class BookingRequest {
    private Long userId;
    private Long hotelId;
    private Long roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalPrice;

    public BookingRequest() {}

    public Long getUserId() { return userId; }
    public Long getHotelId() { return hotelId; }
    public Long getRoomId() { return roomId; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public Double getTotalPrice() { return totalPrice; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}