package com.example.booking_service.dtos;

import com.example.booking_service.enums.EquipmentType;
import lombok.Data;

@Data
public class EquipmentInfo {

    private String equipmentName;
    private double pricePerDay;
    private String description;
    private EquipmentType equipmentType;
    private boolean available;
    private int ownerId;
}
