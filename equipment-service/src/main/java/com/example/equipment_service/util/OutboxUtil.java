package com.example.equipment_service.util;

import com.example.equipment_service.Repository.OutboxRepository;
import com.example.equipment_service.entity.Equipment;
import com.example.equipment_service.entity.Outbox;
import com.example.equipment_service.enums.EventType;
import com.example.equipment_service.enums.OutboxStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class OutboxUtil {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public OutboxUtil(ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }

    public void publishOutboxEvents(EventType eventType,
                                    Equipment equipment,
                                    Object eventPayload) {

        String payload;

        try {
            payload = objectMapper.writeValueAsString(eventPayload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }

        Outbox outbox = new Outbox();
        outbox.setEventType(eventType);
        outbox.setEventId(UUID.randomUUID().toString());
        outbox.setAggregateId(equipment.getId());
        outbox.setAggregateType("Equipment");
        outbox.setPayload(payload);
        outbox.setStatus(OutboxStatus.PENDING);
        outbox.setSagaId(equipment.getSagaId());
        outbox.setCreatedAt(Instant.now());

        outboxRepository.save(outbox);
    }
}
