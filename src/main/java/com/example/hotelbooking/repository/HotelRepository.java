package com.example.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelbooking.entity.Hotel;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationContainingIgnoreCase(String location);
}

