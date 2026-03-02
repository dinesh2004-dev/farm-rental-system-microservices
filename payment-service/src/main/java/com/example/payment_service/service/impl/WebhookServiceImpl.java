package com.example.payment_service.service.impl;

import com.example.payment_service.entity.WebhookEvent;
import com.example.payment_service.enums.WebhookStatus;
import com.example.payment_service.repository.WebhookEventRepository;
import com.example.payment_service.service.WebhookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WebhookServiceImpl implements WebhookService {
    ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(WebhookServiceImpl.class);

    private final WebhookEventRepository webhookEventRepository;

    public WebhookServiceImpl(WebhookEventRepository webhookEventRepository){

        this.webhookEventRepository = webhookEventRepository;
    }
    @Override
    public void process(String payload,String eventId) {

        boolean isPresent = webhookEventRepository.existsById(eventId);

        if(isPresent){

            log.info("Razorpay webhook event with id {} already processed",eventId);
            return;
        }

        JsonNode root;
        try{

            root = objectMapper.readTree(payload);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        String eventId = root.get("id").asText();
        String eventType = root.get("event").asText();
        long createdAt = root.get("created_at").asLong();
        String paymentId = root.get("payload")
                               .get("payment")
                               .get("entity")
                               .get("id").asText();

        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setEventId(eventId);
        webhookEvent.setEventType(eventType);
        webhookEvent.setStatus(WebhookStatus.Received);
        webhookEvent.setCreatedAt(Instant.ofEpochSecond(createdAt));
        webhookEvent.setPayload(payload);
        webhookEvent.setEntityId(paymentId);

        webhookEventRepository.save(webhookEvent);

    }
}
