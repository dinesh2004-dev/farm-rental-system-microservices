package com.example.booking_service.repository;

import com.example.booking_service.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingsRepository extends JpaRepository<Booking,Integer> {


    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.equipment = :equipmentId
    AND :startDate < b.end_date
    AND :endDate > b.start_date
""")
    boolean existsOverlappingBooking(
            @Param("equipmentId") int equipmentId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
