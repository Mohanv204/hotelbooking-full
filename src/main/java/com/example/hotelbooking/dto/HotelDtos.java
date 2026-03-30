package com.example.hotelbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public final class HotelDtos {
    private HotelDtos() {}

    public record HotelDto(
            Long id,
            String name,
            String location,
            String description
    ) {
    }

    public record RoomDto(
            Long id,
            Long hotelId,
            String roomType,
            int capacity,
            java.math.BigDecimal pricePerNight
    ) {
    }

    public record CreateHotelRequest(
            @NotBlank String name,
            @NotBlank String location,
            String description
    ) {
    }

    public record CreateRoomRequest(
            @NotBlank String roomType,
            @NotNull BigDecimal pricePerNight,
            @NotNull int capacity
    ) {
    }
}

