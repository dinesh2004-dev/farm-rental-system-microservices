package com.example.equipment_service.service.impl;

import com.example.equipment_service.Repository.EquipmentRepository;
import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;
import com.example.equipment_service.service.EquipmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public int saveEmployee(EquipmentDTO equipmentDTO) {
        if (equipmentDTO.getEquipmentName() == null || equipmentDTO.getEquipmentName().trim().isEmpty()) {
            throw new IllegalArgumentException("equipmentName must not be null or empty");
        }

        Equipment equipment = mapper.map(equipmentDTO, Equipment.class);
        System.out.println("id before save: " + equipment.getId());
//        equipment.setId(0); // Ensure id is 0 so Hibernate will insert, not update
        equipmentRepository.save(equipment);
        return equipment.getId();
    }

    @Override
    public EquipmentDTO getEquipmentById(int id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
        return mapper.map(equipment, EquipmentDTO.class);
    }

    @Override
    public List<EquipmentDTO> getEquipmentsByUserId(int userId) {
        List<Equipment> equipments = equipmentRepository.findByOwnerId(userId);
        return equipments.stream()
                .map(equipment -> mapper.map(equipment, EquipmentDTO.class))
                .collect(Collectors.toList());
    }
}
