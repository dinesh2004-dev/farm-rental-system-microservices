package com.example.booking_service.service.impl;

import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.repository.BookingsRepository;
import com.example.booking_service.service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int createBooking(BookingsDTO bookingsDTO) {

        Booking booking = modelMapper.map(bookingsDTO,Booking.class);
        bookingsRepository.save(booking);
        return booking.getId();

    }
}
