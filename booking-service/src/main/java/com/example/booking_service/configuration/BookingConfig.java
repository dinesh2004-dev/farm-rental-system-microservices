package com.example.booking_service.configuration;

import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.entity.Booking;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(BookingsDTO.class, Booking.class)
                .addMappings(mapper -> mapper.skip(Booking::setId))
                .addMappings(mapper -> mapper.skip(Booking::setRenter));
        return modelMapper;
    }
}
