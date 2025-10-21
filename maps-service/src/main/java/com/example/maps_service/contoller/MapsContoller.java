package com.example.maps_service.contoller;

import com.example.maps_service.dto.CoordinatesDto;
import com.example.maps_service.service.MapsService;
import com.example.maps_service.service.impl.MapsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/maps")
public class MapsContoller{

    @Autowired
    private MapsServiceImpl mapService;

    @GetMapping("/geocode")
    public ResponseEntity<CoordinatesDto> geocodeAddress(@RequestParam String address) {
        CoordinatesDto coordinates = mapService.getCoordinates(address);
        return ResponseEntity.status(HttpStatus.OK).body(coordinates);
    }

    @GetMapping("/reverseGeocode")
    public ResponseEntity<String> reverseGeocode(@RequestParam double latitude,@RequestParam double longitude){

        String address = mapService.getAddressFromCoordinates(latitude, longitude);
        return ResponseEntity.status(HttpStatus.OK).body(address);
    }
}
