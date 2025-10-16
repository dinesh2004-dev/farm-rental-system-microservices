package com.example.equipment_service.service;

import com.example.equipment_service.dtos.EquipmentDTO;

public interface EquipmentService {

    int saveEmployee(EquipmentDTO equipmentDTO);

    EquipmentDTO getEquipmentById(int id);
}
