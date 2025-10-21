package com.example.maps_service.service;

import com.example.maps_service.dto.CoordinatesDto;

public interface MapsService {
    CoordinatesDto getCoordinates(String address);
    String getAddressFromCoordinates(double latitude, double longitude);
}
