package com.example.equipment_service.Repository;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    List<Equipment> findByOwnerId(int userId);

    @Modifying
    @Query("""
        UPDATE Equipment e
           SET e.available = false
         WHERE e.id = :equipmentId
           AND e.available = true
    """)
    int reserveIfAvailable(@Param("equipmentId") int equipmentId);
}
