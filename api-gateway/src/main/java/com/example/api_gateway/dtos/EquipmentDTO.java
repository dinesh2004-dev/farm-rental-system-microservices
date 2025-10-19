package com.example.api_gateway.dtos;

import com.example.api_gateway.enums.EquipmentType;
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