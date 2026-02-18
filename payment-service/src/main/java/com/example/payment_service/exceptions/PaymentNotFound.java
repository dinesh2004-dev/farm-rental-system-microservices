package com.example.payment_service.exceptions;

public class PaymentNotFound extends Exception{

    public PaymentNotFound(String message){
        super(message);
    }
}
