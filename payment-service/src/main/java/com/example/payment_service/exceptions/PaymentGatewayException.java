package com.example.payment_service.exceptions;

import com.razorpay.RazorpayException;

public class PaymentGatewayException extends Exception{

    public PaymentGatewayException(String message, RazorpayException e){
        super(message);
    }
}
