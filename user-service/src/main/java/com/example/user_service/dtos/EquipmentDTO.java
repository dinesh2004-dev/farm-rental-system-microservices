package com.example.user_service.dtos;

import com.example.user_service.entity.EquipmentType;
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
