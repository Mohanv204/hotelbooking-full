package com.hotel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Double pricePerNight;

    @Column(nullable = false)
    private Integer capacity;

    private String amenities;
    private String imageUrl;

    @Builder.Default
    private Boolean isAvailable = true;

    public enum RoomType { STANDARD, DELUXE, SUITE, PENTHOUSE }
}
