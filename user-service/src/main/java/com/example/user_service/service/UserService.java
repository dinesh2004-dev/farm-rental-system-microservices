package com.example.user_service.service;

import com.example.user_service.dtos.EquipmentDTO;
import com.example.user_service.dtos.UserDTO;
import com.example.user_service.entity.User;

import java.util.List;

public interface UserService {

    int saveUser(UserDTO userDTO);
    UserDTO getUserById(int id);
    List<EquipmentDTO> getEquipmentsByUserId(int userId);
}
