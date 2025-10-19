package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient userServiceWebClient(@Value("${user.service.url}") String baseUrl){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient equipmentServiceWebClient(@Value("${equipment.service.url}") String baseUrl){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
