package com.example.api_gateway.dtos;

import lombok.Data;

import java.util.List;

@Data
public class UserRentalDetailsDto {

    private UserDTO userProfile;
    private List<EquipmentDTO> rentalEquipment;

    public UserRentalDetailsDto(UserDTO t1, List<EquipmentDTO> t2) {
        this.userProfile = t1;
        this.rentalEquipment = t2;
    }
}
