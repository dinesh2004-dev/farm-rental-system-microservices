package com.example.equipment_service.config;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(EquipmentDTO.class, Equipment.class)
                .addMappings(mapper -> mapper.skip(Equipment::setId));
        return modelMapper;
    }
}
