package com.hotel.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
@Data
public class CreateBookingRequest {
    @NotNull private Long roomId;
    @NotNull private Long hotelId;
    @NotNull private LocalDate checkInDate;
    @NotNull private LocalDate checkOutDate;
    @NotNull @Min(1) private Integer guests;
}
