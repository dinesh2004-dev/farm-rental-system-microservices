package com.example.equipment_service.producer;

import ch.qos.logback.core.util.FixedDelay;
import com.example.equipment_service.Repository.OutboxRepository;
import com.example.equipment_service.entity.Outbox;
import com.example.equipment_service.enums.EventType;
import com.example.equipment_service.enums.OutboxStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxProducer {


    private final OutboxRepository outboxRepository;

    private final KafkaTemplate<String,String> kafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(
            OutboxProducer.class);

    public OutboxProducer(OutboxRepository outboxRepository,
                          @Qualifier("outboxKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents(){

        List<Outbox> outboxList = outboxRepository.findTop50ByStatusOrderByCreatedAt(
                OutboxStatus.PENDING
        );

        for(Outbox  outbox : outboxList){

            kafkaTemplate.send(
                    resolveTopic(outbox),
                    outbox.getSagaId(),
                    outbox.getPayload()
            ).whenComplete((result,ex) -> {
                if(ex == null){

                    outbox.setStatus(OutboxStatus.SENT);
                    outbox.setPublishedAt(Instant.now());
                    outboxRepository.save(outbox);
                }
                else{

                    log.error("Kafka publish failed for outbox id {}", outbox.getId(), ex);
                }
            });
        }
    }

    private String resolveTopic(Outbox outbox){

        return switch (outbox.getEventType().toString()){
            case  "EQUIPMENT_RESERVED"-> "equipment-reserved-event";
            case "EQUIPMENT_RESERVATION_FAILED" -> "equipment-reservation-failed-event";
            default -> throw new IllegalArgumentException("Unknown event type: " + outbox.getEventType());
        };


    }
}
