package com.example.booking_service.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapingConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
