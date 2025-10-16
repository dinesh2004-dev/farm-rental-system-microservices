package com.example.equipment_service.dtos;

import com.example.equipment_service.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class EquipmentDTO {


    private String equipmentName;
    private double pricePerDay;
    private String description;
    private EquipmentType equipmentType;
    private boolean available;
    private int ownerId;
}
