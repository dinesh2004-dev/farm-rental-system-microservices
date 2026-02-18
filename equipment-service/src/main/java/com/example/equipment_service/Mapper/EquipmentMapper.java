package com.example.equipment_service.Mapper;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sagaId", ignore = true)
    @Mapping(target ="available", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    Equipment equipmentDtoToEquipment(EquipmentDTO equipmentDTO);

    EquipmentDTO equipmentToEquipmentDto(Equipment equipment);
}
