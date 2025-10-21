package com.example.user_service.clients;

import com.example.user_service.dtos.CoordinatesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "maps-service")
public interface MapsServiceClient {

    @GetMapping("/maps/geocode")
    CoordinatesDto geocodeAddress(@RequestParam String address);

    @GetMapping("/maps/reverseGeocode")
    String reverseGeocode(@RequestParam double latitude,@RequestParam double longitude);
}
