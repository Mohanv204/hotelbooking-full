package com.example.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotelbooking.entity.Room;

import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
            select r from Room r
            where r.hotel.id = :hotelId
            and not exists (
              select b from Booking b
              where b.room.id = r.id
              and b.status = 'CONFIRMED'
              and b.checkIn < :checkOut
              and b.checkOut > :checkIn
            )
            """)
    List<Room> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Room r where r.id = :id")
    Optional<Room> findByIdForUpdate(@Param("id") Long id);
}

