package com.example.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hotelbooking.entity.Booking;
import com.example.hotelbooking.entity.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select (count(b) > 0)
            from Booking b
            where b.room.id = :roomId
              and b.status = :status
              and b.checkIn < :checkOut
              and b.checkOut > :checkIn
            """)
    boolean existsOverlapping(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("status") BookingStatus status
    );

    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    List<Booking> findAllByUser_Id(Long userId);
}

