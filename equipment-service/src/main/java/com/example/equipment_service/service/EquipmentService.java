package com.example.equipment_service.service;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;

import java.util.List;

public interface EquipmentService {

    int saveEquipment(EquipmentDTO equipmentDTO);

    Equipment getEquipmentById(int id);

    List<EquipmentDTO> getEquipmentsByUserId(int userId);

    boolean reserveEquipment(int equipmentId);

    EquipmentDTO getEquipmentDTOById(int id);
}
