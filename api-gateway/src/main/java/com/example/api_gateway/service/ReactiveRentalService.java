package com.example.api_gateway.service;

import com.example.api_gateway.dtos.EquipmentDTO;
import com.example.api_gateway.dtos.UserDTO;
import com.example.api_gateway.dtos.UserRentalDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReactiveRentalService {

    @Autowired
    @Qualifier("userServiceWebClient")
    private WebClient userWebClient;
    @Autowired
    @Qualifier("equipmentServiceWebClient")
    private WebClient equipmentWebClient;



    public Mono<UserRentalDetailsDto> getUserRentalDetails(int userId){

        Mono<UserDTO> userMono = getUserDetails(userId);
        Mono<List<EquipmentDTO>> equipmentListMono = getEquipmentForUser(userId).collectList();

        return Mono.zip(userMono, equipmentListMono)
                .map(tuple -> new UserRentalDetailsDto(tuple.getT1(), tuple.getT2()));
    }

    public Mono<UserDTO> getUserDetails(int userId){
        return userWebClient.get()
                .uri("/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .onErrorResume(ex -> Mono.empty());
    }

    public Flux<EquipmentDTO> getEquipmentForUser(int userId){
        return equipmentWebClient.get()
                .uri("/equipments/user/{userId}",userId)
                .retrieve()
                .bodyToFlux(EquipmentDTO.class);


    }


}
