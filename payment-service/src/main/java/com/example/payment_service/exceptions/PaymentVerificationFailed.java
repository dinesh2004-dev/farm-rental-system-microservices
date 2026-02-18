package com.example.payment_service.exceptions;

public class PaymentVerificationFailed extends Exception{
    public PaymentVerificationFailed(String message){
        super(message);
    }
}
