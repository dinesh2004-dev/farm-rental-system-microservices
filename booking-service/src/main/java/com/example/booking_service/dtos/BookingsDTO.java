package com.example.booking_service.dtos;

import com.example.booking_service.enums.BookingStatus;
import com.example.booking_service.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingsDTO {


    private int lender;
    private int equipment;
    private int renter;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private double totalCost;
}
