package com.hotel.dto;
import com.hotel.entity.Room;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CreateRoomRequest {
    @NotNull private Long hotelId;
    @NotNull private Room.RoomType roomType;
    @NotNull @Positive private Double pricePerNight;
    @NotNull @Min(1) private Integer capacity;
    private String amenities;
    private String imageUrl;
}
