package com.example.user_service.contoller;

import com.example.user_service.dtos.EquipmentDTO;
import com.example.user_service.dtos.UserDTO;
import com.example.user_service.service.impl.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserContoller {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody UserDTO userDTO){
        int id = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully with id: "+id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id){

        UserDTO userDTO = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/{userId}/equipments")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentsByUserId(@PathVariable int userId) {
        List<EquipmentDTO> equipmentDTOs = userService.getEquipmentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(equipmentDTOs);
    }




}
