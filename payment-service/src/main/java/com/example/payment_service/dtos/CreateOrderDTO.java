package com.example.payment_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {

    private int amount;
    private String currency;
    private String OrderId;
    private String key;
}
