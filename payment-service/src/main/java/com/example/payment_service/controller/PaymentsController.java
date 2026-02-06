package com.example.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class Payments {

    @GetMapping("/test")
    public ResponseEntity<String> testPaymentService(){
        return ResponseEntity.ok("Payment Service is up and running");
    }
}
