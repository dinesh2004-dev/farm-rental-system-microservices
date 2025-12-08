package com.example.booking_service.service;

import com.example.booking_service.dtos.BookingsDTO;

public interface BookingService {
    int createBooking(BookingsDTO bookingsDTO);
}
