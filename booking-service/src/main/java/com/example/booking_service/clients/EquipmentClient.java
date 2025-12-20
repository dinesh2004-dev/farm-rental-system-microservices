package com.example.booking_service.clients;

import com.example.booking_service.dtos.EquipmentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "equipment-service")
public interface EquipmentClient {

    @GetMapping("api/equipments/{id}")
    public  EquipmentInfo getEquipmentById(@PathVariable("id") int id);
}
