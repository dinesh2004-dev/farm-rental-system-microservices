package com.example.payment_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentVerifyDTO {

    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;

}
