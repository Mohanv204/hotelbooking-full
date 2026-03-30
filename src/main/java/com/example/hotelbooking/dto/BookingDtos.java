package com.example.hotelbooking.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public final class BookingDtos {
    private BookingDtos() {}

    public record CreateBookingRequest(
            @NotNull Long roomId,
            @NotNull LocalDate checkIn,
            @NotNull LocalDate checkOut
    ) {
    }

    public record BookingDto(
            Long id,
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            String status
    ) {
    }
}

