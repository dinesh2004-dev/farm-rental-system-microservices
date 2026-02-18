package com.example.booking_service.service;

import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.exceptions.BookingNotFoundException;
import org.example.events.PaymentFailedEvent;
import org.example.events.PaymentSuccessEvent;

public interface BookingService {
    int createBooking(BookingsDTO bookingsDTO);
    void handlePaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent) throws BookingNotFoundException;
    void handlePaymentFailureEvent(PaymentFailedEvent paymentFailedEvent);
}
