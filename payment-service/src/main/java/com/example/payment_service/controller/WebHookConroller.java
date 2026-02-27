package com.example.payment_service.controller;

import com.example.payment_service.service.WebhookService;
import com.example.payment_service.util.HmacSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("webhook")
public class WebHookConroller {

    private static final Logger log = LoggerFactory.getLogger(WebHookConroller.class);

    @Value("${webhook.secret.key}")
    private String secret;

    private final WebhookService webhookService;

    public  WebHookConroller(WebhookService webhookService){

        this.webhookService = webhookService;
    }

    @PostMapping("razorpay")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("X-Razorpay-Signature") String signature,
                                                @RequestHeader("x-razorpay-event-id") String eventId) throws NoSuchAlgorithmException, InvalidKeyException {

        String expectedSign = HmacSigner.calculateHmac(secret,payload);

        boolean isValid = MessageDigest.isEqual(
                expectedSign.getBytes(),
                signature.getBytes()
        );

        if(!isValid){

            log.info("Failed");
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid");

        }

        log.info("success");

        log.info(payload);
        webhookService.process(payload,eventId);

        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
}
