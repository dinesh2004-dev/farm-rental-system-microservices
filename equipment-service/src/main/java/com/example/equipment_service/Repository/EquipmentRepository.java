package com.example.equipment_service.Repository;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    List<Equipment> findByOwnerId(int userId);
}
