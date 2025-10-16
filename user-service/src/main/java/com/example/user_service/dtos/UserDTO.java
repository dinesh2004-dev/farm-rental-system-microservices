package com.example.user_service.dtos;

import com.example.user_service.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String email;
    private Role role;
    private String password;
    private String phoneNumber;
}
