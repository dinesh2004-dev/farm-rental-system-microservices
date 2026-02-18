package com.example.booking_service.exceptions;

public class BookingNotFoundException extends Exception{

    public BookingNotFoundException(String message){
        super(message);
    }
}
