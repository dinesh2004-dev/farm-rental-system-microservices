package com.example.payment_service.controller;

import com.example.payment_service.dtos.CreateOrderDTO;
import com.example.payment_service.dtos.CreateOrderRequest;
import com.example.payment_service.dtos.PaymentVerifyDTO;
import com.example.payment_service.exceptions.PaymentGatewayException;
import com.example.payment_service.exceptions.PaymentNotFound;
import com.example.payment_service.exceptions.PaymentNotInitiated;
import com.example.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    private final Logger log = LoggerFactory.getLogger(PaymentsController.class);
    public PaymentsController(PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderDTO> createOrder(@RequestBody CreateOrderRequest createOrderRequest) throws PaymentNotFound, PaymentNotInitiated, PaymentGatewayException {

        CreateOrderDTO createOrderDTO = paymentService.createOrder(createOrderRequest.getBookingId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(@RequestBody PaymentVerifyDTO paymentVerifyDTO) throws PaymentNotFound, PaymentNotInitiated, PaymentGatewayException {

        log.info("Verifying payment for orderId: {}", paymentVerifyDTO.getRazorpayOrderId());
        paymentService.verifyPayment(paymentVerifyDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
