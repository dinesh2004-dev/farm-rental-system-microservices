package com.example.equipment_service.service;

import com.example.equipment_service.dtos.EquipmentDTO;

import java.util.List;

public interface EquipmentService {

    int saveEmployee(EquipmentDTO equipmentDTO);

    EquipmentDTO getEquipmentById(int id);

    List<EquipmentDTO> getEquipmentsByUserId(int userId);
}
