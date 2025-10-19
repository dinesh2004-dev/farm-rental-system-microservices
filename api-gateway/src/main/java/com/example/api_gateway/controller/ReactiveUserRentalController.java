package com.example.api_gateway.controller;

import com.example.api_gateway.dtos.UserRentalDetailsDto;
import com.example.api_gateway.service.ReactiveRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user-rentals")
public class ReactiveUserRentalController {

    @Autowired
    private ReactiveRentalService reactiveRentalService;

    @GetMapping("/{userId}")
    public Mono<UserRentalDetailsDto> getCompositeDetails(@PathVariable int userId) {
        // The controller's job is just to call the service and return the Mono.
        // The framework handles the rest!
        return reactiveRentalService.getUserRentalDetails(userId);
    }
}
