package com.hotel.dto;
import com.hotel.entity.Room;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String hotelName;
    private Room.RoomType roomType;
    private Double pricePerNight;
    private Integer capacity;
    private String amenities;
    private String imageUrl;
    private Boolean isAvailable;
    public static RoomDto from(Room r) {
        return RoomDto.builder()
                .id(r.getId()).hotelId(r.getHotel().getId())
                .hotelName(r.getHotel().getName())
                .roomType(r.getRoomType()).pricePerNight(r.getPricePerNight())
                .capacity(r.getCapacity()).amenities(r.getAmenities())
                .imageUrl(r.getImageUrl()).isAvailable(r.getIsAvailable()).build();
    }
}
