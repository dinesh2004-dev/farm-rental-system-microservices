package com.example.user_service.config;

import com.example.user_service.dtos.UserDTO;
import com.example.user_service.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public ModelMapper modelMapper(){

        return new ModelMapper();

    }

}
