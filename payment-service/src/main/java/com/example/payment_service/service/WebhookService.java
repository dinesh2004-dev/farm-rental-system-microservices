package com.example.payment_service.service;

public interface WebhookService {

    void process(String payload,String eventId);
}
