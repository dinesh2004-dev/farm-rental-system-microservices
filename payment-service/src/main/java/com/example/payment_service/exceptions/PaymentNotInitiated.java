package com.example.payment_service.exceptions;

public class PaymentNotInitiated extends Exception{

    public PaymentNotInitiated(String message){
        super(message);
    }
}
