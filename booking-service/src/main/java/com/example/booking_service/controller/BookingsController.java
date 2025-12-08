package com.example.booking_service.controller;

import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.service.BookingService;
import com.example.booking_service.service.impl.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @GetMapping("/create")
    public ResponseEntity<Integer> createBooking(@RequestBody BookingsDTO bookingsDTO){

        int id = bookingServiceImpl.createBooking(bookingsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body((id));

    }
}
