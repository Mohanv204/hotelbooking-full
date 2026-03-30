package com.hotel.config;

import com.hotel.entity.*;
import com.hotel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final HotelRepository hotelRepo;
    private final RoomRepository roomRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        try {
            if (userRepo.existsByEmail("admin@hotel.com")) { log.info("Data already seeded. Skipping."); return; }

            userRepo.save(User.builder().name("Hotel Admin").email("admin@hotel.com")
                    .password(encoder.encode("admin123")).role(User.Role.ADMIN).phone("9000000000").build());

            userRepo.save(User.builder().name("Mohan Kumar").email("user@hotel.com")
                    .password(encoder.encode("user123")).role(User.Role.USER).phone("9111111111").build());

            Hotel h1 = hotelRepo.save(Hotel.builder().name("The Grand Palace").location("Mumbai")
                    .description("Luxury 5-star hotel in the heart of Mumbai with stunning sea views.")
                    .rating(4.8).amenities("Pool, Spa, Gym, Restaurant, WiFi, Parking")
                    .imageUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800").isActive(true).build());

            Hotel h2 = hotelRepo.save(Hotel.builder().name("Ocean View Resort").location("Goa")
                    .description("Beachfront resort with private beach access and water sports.")
                    .rating(4.5).amenities("Beach, Pool, Water Sports, Bar, Restaurant, WiFi")
                    .imageUrl("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800").isActive(true).build());

            Hotel h3 = hotelRepo.save(Hotel.builder().name("Mountain Retreat").location("Shimla")
                    .description("Peaceful mountain getaway with breathtaking Himalayan views.")
                    .rating(4.3).amenities("Mountain View, Bonfire, Trekking, Restaurant, WiFi")
                    .imageUrl("https://images.unsplash.com/photo-1519449556851-5720b33024e7?w=800").isActive(true).build());

            Hotel h4 = hotelRepo.save(Hotel.builder().name("City Center Hotel").location("Bangalore")
                    .description("Modern business hotel in the IT hub of India.")
                    .rating(4.2).amenities("Business Center, Gym, Restaurant, WiFi, Parking")
                    .imageUrl("https://images.unsplash.com/photo-1571003123894-1f0594d2b5d9?w=800").isActive(true).build());

            seedRooms(h1, 2000.0, 4500.0, 9000.0); seedRooms(h2, 1800.0, 3500.0, 7500.0);
            seedRooms(h3, 1500.0, 3000.0, 6000.0); seedRooms(h4, 1200.0, 2500.0, 5000.0);

            log.info("✅ Hotel DB seeded  |  admin@hotel.com / admin123  |  user@hotel.com / user123");
        } catch (Exception e) {
            log.error("DataSeeder failed: {}", e.getMessage(), e);
        }
    }

    private void seedRooms(Hotel h, double std, double dlx, double suite) {
        roomRepo.save(Room.builder().hotel(h).roomType(Room.RoomType.STANDARD).pricePerNight(std).capacity(2).amenities("AC, TV, WiFi, Hot Water").isAvailable(true).build());
        roomRepo.save(Room.builder().hotel(h).roomType(Room.RoomType.STANDARD).pricePerNight(std).capacity(2).amenities("AC, TV, WiFi, Hot Water").isAvailable(true).build());
        roomRepo.save(Room.builder().hotel(h).roomType(Room.RoomType.DELUXE).pricePerNight(dlx).capacity(3).amenities("AC, TV, WiFi, Minibar, Balcony").isAvailable(true).build());
        roomRepo.save(Room.builder().hotel(h).roomType(Room.RoomType.SUITE).pricePerNight(suite).capacity(4).amenities("AC, TV, WiFi, Minibar, Jacuzzi, Living Room").isAvailable(true).build());
    }
}
