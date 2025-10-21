package com.example.maps_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:config.properties")
public class MapsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapsServiceApplication.class, args);
	}

}
