package com.example.equipment_service.entity;


import com.example.equipment_service.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "equipment_table")
@Data
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;
    @Column(name = "price_per_day", nullable = false)
    private double pricePerDay;
    @Column(name = "description", nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false)
    private EquipmentType equipmentType;
    @Column(name = "available", nullable = false)
    private boolean available;
    @Column(name = "owner_id", nullable = false)
    private int ownerId;

}
