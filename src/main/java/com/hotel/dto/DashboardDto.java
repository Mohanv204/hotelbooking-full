package com.hotel.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardDto {
    private long totalBookings;
    private long confirmedBookings;
    private long cancelledBookings;
    private long completedBookings;
    private long totalHotels;
    private long totalUsers;
    private double totalRevenue;
}
