package com.example.user_service.dtos;

import lombok.Data;

@Data
public class CoordinatesDto {

    private double latitude;
    private double longitude;
    private String address;


    public CoordinatesDto(double latitude, double longitude,String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}
