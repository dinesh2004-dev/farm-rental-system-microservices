package com.example.payment_service.util;

import com.example.payment_service.entity.Outbox;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.enums.OutboxStatus;
import com.example.payment_service.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class OutboxUtil {

    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private  OutboxRepository outboxRepository;

    public void publishOutboxEvents(String eventType,
                                    Payment payment,
                                    Object eventPayload){
        String payload;
         try {
             payload = objectMapper.writeValueAsString(
                     eventPayload
             );
         }
         catch (Exception e){
             throw new RuntimeException("Failed to serialize event payload",e);
         }
         Outbox outbox = new Outbox();
         outbox.setEventType(eventType);
         outbox.setEventId(UUID.randomUUID().toString());
         outbox.setPayload(payload);
         outbox.setAggregateId(payment.getId());
         outbox.setAggregateType("Payment");
         outbox.setStatus(OutboxStatus.PENDING);
         outbox.setCreatedAt(Instant.now());

         outboxRepository.save(outbox);
    }
}
