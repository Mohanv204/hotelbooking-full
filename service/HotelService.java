package com.hotelbooking.app.hotelbooking.service;

import com.hotelbooking.app.hotelbooking.entity.Hotel;
import com.hotelbooking.app.hotelbooking.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// REMOVED: @RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    // ADDED: Manual constructor for injection
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));
    }

    public Hotel addHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Hotel existing = getHotelById(id);
        existing.setName(updatedHotel.getName());
        existing.setLocation(updatedHotel.getLocation());
        existing.setDescription(updatedHotel.getDescription());
        existing.setImageUrl(updatedHotel.getImageUrl());
        return hotelRepository.save(existing);
    }

    public String deleteHotel(Long id) {
        hotelRepository.deleteById(id);
        return "Hotel deleted successfully!";
    }

    public List<Hotel> searchByLocation(String location) {
        return hotelRepository.findByLocation(location);
    }
}