package com.example.equipment_service.controller;

import com.example.equipment_service.dtos.EquipmentDTO;
import com.example.equipment_service.service.impl.EquipmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentContoller {

    @Autowired
    private EquipmentServiceImpl equipmentService;

    @PostMapping("/save")
    public ResponseEntity<String> saveEquipment(@RequestBody EquipmentDTO equipmentDTO){

        int id = equipmentService.saveEmployee(equipmentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Equipment saved successfully with id: "+id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable int id){

        EquipmentDTO equipmentDTO = equipmentService.getEquipmentById(id);

        return ResponseEntity.status(HttpStatus.OK).body(equipmentDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentsByUserId(@PathVariable int userId) {
        List<EquipmentDTO> equipmentDTOs = equipmentService.getEquipmentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(equipmentDTOs);
    }
}
