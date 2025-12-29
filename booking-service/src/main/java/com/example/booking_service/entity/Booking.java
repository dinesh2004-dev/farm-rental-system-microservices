package com.example.booking_service.entity;

import com.example.booking_service.enums.BookingStatus;
import com.example.booking_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "equipment_id",nullable = false)
    private int equipment;
    @Column(name = "renter_id",nullable = false)
    private int renter;
    @Column(nullable = false)
    private LocalDateTime start_date;
    @Column(nullable = false)
    private LocalDateTime end_date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    @Column(nullable = false)
    private double totalCost;
    @Column(nullable = false)
    private String SagaId;


}
