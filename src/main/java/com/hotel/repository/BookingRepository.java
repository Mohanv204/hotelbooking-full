package com.hotel.repository;
import com.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    List<Booking> findAllByOrderByBookedAtDesc();
    long countByStatus(Booking.BookingStatus status);
    @Query("SELECT COALESCE(SUM(b.totalPrice),0) FROM Booking b WHERE b.status='CONFIRMED' OR b.status='COMPLETED'")
    Double getTotalRevenue();
}
