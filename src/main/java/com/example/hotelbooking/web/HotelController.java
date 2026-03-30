package com.example.hotelbooking.web;

import com.example.hotelbooking.dto.HotelDtos;
import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.entity.Room;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/hotels")
@Validated
public class HotelController {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    public HotelController(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public List<HotelDtos.HotelDto> listHotels(@RequestParam(required = false) String location) {
        List<Hotel> hotels = (location == null || location.isBlank())
                ? hotelRepository.findAll()
                : hotelRepository.findByLocationContainingIgnoreCase(location);

        return hotels.stream()
                .map(h -> new HotelDtos.HotelDto(h.getId(), h.getName(), h.getLocation(), h.getDescription()))
                .toList();
    }

    @PostMapping
    public HotelDtos.HotelDto createHotel(@Valid @RequestBody HotelDtos.CreateHotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.name());
        hotel.setLocation(request.location());
        hotel.setDescription(request.description());
        Hotel saved = hotelRepository.save(hotel);
        return new HotelDtos.HotelDto(saved.getId(), saved.getName(), saved.getLocation(), saved.getDescription());
    }

    @GetMapping("/{hotelId}/rooms")
    public List<HotelDtos.RoomDto> listAvailableRooms(
            @PathVariable Long hotelId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new ResponseStatusException(BAD_REQUEST, "checkOut must be after checkIn.");
        }

        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Hotel not found."));

        List<Room> rooms = roomRepository.findAvailableRooms(hotelId, checkIn, checkOut);

        return rooms.stream()
                .map(r -> new HotelDtos.RoomDto(
                        r.getId(),
                        hotelId,
                        r.getRoomType(),
                        r.getCapacity(),
                        r.getPricePerNight()
                ))
                .toList();
    }

    @PostMapping("/{hotelId}/rooms")
    public HotelDtos.RoomDto createRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody HotelDtos.CreateRoomRequest request
    ) {
        if (request.capacity() <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "capacity must be positive.");
        }
        if (request.pricePerNight() == null || request.pricePerNight().signum() <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "pricePerNight must be positive.");
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Hotel not found."));

        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(request.roomType());
        room.setCapacity(request.capacity());
        room.setPricePerNight(request.pricePerNight());

        Room saved = roomRepository.save(room);

        return new HotelDtos.RoomDto(saved.getId(), hotelId, saved.getRoomType(), saved.getCapacity(), saved.getPricePerNight());
    }
}

