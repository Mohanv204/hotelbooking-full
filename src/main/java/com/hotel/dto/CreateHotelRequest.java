package com.hotel.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class CreateHotelRequest {
    @NotBlank private String name;
    @NotBlank private String location;
    private String description;
    private Double rating;
    private String amenities;
    private String imageUrl;
}
