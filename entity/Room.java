package com.hotelbooking.app.hotelbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private Double pricePerNight;

    @Column(nullable = false)
    private Boolean available;

    // Constructors
    public Room() {}

    // Getters
    public Long getId() { return id; }
    public Hotel getHotel() { return hotel; }
    public String getRoomType() { return roomType; }
    public Double getPricePerNight() { return pricePerNight; }
    public Boolean getAvailable() { return available; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setPricePerNight(Double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setAvailable(Boolean available) { this.available = available; }
}