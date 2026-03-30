package com.hotel.dto;
import com.hotel.entity.Hotel;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String location;
    private String description;
    private Double rating;
    private String amenities;
    private String imageUrl;
    private Boolean isActive;
    public static HotelDto from(Hotel h) {
        return HotelDto.builder()
                .id(h.getId()).name(h.getName()).location(h.getLocation())
                .description(h.getDescription()).rating(h.getRating())
                .amenities(h.getAmenities()).imageUrl(h.getImageUrl())
                .isActive(h.getIsActive()).build();
    }
}
