package com.example.payment_service.service;


import com.example.payment_service.dtos.CreateOrderDTO;
import com.example.payment_service.dtos.PaymentVerifyDTO;
import com.example.payment_service.exceptions.PaymentGatewayException;
import com.example.payment_service.exceptions.PaymentNotFound;
import com.example.payment_service.exceptions.PaymentNotInitiated;
import com.example.payment_service.exceptions.PaymentVerificationFailed;
import org.example.events.BookingCreatedEvent;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface PaymentService {

      CreateOrderDTO createOrder(int bookingId) throws PaymentNotFound,
              PaymentNotInitiated, PaymentGatewayException;
      int initiatePayment(BookingCreatedEvent event);

      void verifyPayment(PaymentVerifyDTO paymentVerifyDTO) throws PaymentNotFound;


}
