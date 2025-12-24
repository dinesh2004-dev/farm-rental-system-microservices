package com.example.equipment_service.events;

import com.example.equipment_service.entity.Equipment;
import com.example.equipment_service.enums.EquipmentType;
import lombok.Data;

import java.time.Instant;

@Data
public class EquipmentEvent {
    private int id;
    private String name;
    private double pricePerDay;
    private String description;
    private EquipmentType equipmentType;
    private boolean available;
    private int ownerId;
    private Instant createdAt = Instant.now();

    public EquipmentEvent(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getEquipmentName();
        this.pricePerDay = equipment.getPricePerDay();
        this.description = equipment.getDescription();
        this.equipmentType = equipment.getEquipmentType();
        this.available = equipment.isAvailable();
        this.ownerId = equipment.getOwnerId();
        this.createdAt = Instant.now();
    }

}
