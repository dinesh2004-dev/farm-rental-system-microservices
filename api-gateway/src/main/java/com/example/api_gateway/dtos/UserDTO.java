package com.example.api_gateway.dtos;
import com.example.api_gateway.enums.Role;
import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String email;
    private Role role;
    private String password;
    private String phoneNumber;
}