package com.example.user_service.entity;

import com.example.user_service.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_table")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "full_name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "Email_id", nullable = false, unique = true)
    private String email;
    @Column(name = "mobile", nullable = false, length = 10, unique = true)
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(name = "Address", nullable = false)
    private String address;


}
