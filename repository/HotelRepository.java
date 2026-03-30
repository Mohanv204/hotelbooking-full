package com.hotelbooking.app.hotelbooking.repository;

import com.hotelbooking.app.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByLocation(String location);

    List<Hotel> findByNameContainingIgnoreCase(String name);
}
