package com.example.payment_service.dtos;

public class CreateOrderRequest {

    private int bookingId;

    public int getBookingId(){
        return bookingId;
    }

    public void setBookingId(int bookingId){
        this.bookingId = bookingId;
    }
}
