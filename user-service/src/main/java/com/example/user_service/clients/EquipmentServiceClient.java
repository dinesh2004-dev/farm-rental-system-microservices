package com.example.user_service.clients;

import com.example.user_service.dtos.EquipmentDTO;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "equipment-service")
public interface EquipmentServiceClient {

    @GetMapping("/equipments/user/{userId}")
    List<EquipmentDTO> getEquipmentsByUserId(@PathVariable("userId") int userId);
}
