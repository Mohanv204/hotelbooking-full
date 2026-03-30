package com.hotel.service;

import com.hotel.dto.*;
import com.hotel.entity.Hotel;
import com.hotel.entity.Room;
import com.hotel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepo;
    private final RoomRepository roomRepo;

    public List<HotelDto> getAllHotels() {
        return hotelRepo.findByIsActiveTrue().stream().map(HotelDto::from).collect(Collectors.toList());
    }

    public List<HotelDto> searchHotels(String location) {
        return hotelRepo.findByLocationContainingIgnoreCaseAndIsActiveTrue(location)
                .stream().map(HotelDto::from).collect(Collectors.toList());
    }

    public HotelDto getHotelById(Long id) {
        return HotelDto.from(hotelRepo.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found")));
    }

    public List<RoomDto> getRoomsByHotel(Long hotelId) {
        return roomRepo.findByHotelId(hotelId).stream().map(RoomDto::from).collect(Collectors.toList());
    }

    public List<RoomDto> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepo.findAvailableRooms(hotelId, checkIn, checkOut).stream().map(RoomDto::from).collect(Collectors.toList());
    }

    @Transactional
    public HotelDto createHotel(CreateHotelRequest req) {
        Hotel hotel = hotelRepo.save(Hotel.builder()
                .name(req.getName()).location(req.getLocation()).description(req.getDescription())
                .rating(req.getRating()).amenities(req.getAmenities()).imageUrl(req.getImageUrl())
                .isActive(true).build());
        return HotelDto.from(hotel);
    }

    @Transactional
    public HotelDto updateHotel(Long id, CreateHotelRequest req) {
        Hotel hotel = hotelRepo.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.setName(req.getName()); hotel.setLocation(req.getLocation());
        hotel.setDescription(req.getDescription()); hotel.setRating(req.getRating());
        hotel.setAmenities(req.getAmenities()); hotel.setImageUrl(req.getImageUrl());
        return HotelDto.from(hotelRepo.save(hotel));
    }

    @Transactional
    public RoomDto addRoom(CreateRoomRequest req) {
        Hotel hotel = hotelRepo.findById(req.getHotelId()).orElseThrow(() -> new RuntimeException("Hotel not found"));
        Room room = roomRepo.save(Room.builder()
                .hotel(hotel).roomType(req.getRoomType()).pricePerNight(req.getPricePerNight())
                .capacity(req.getCapacity()).amenities(req.getAmenities())
                .imageUrl(req.getImageUrl()).isAvailable(true).build());
        return RoomDto.from(room);
    }

    public List<HotelDto> getAllHotelsAdmin() {
        return hotelRepo.findAll().stream().map(HotelDto::from).collect(Collectors.toList());
    }
}
