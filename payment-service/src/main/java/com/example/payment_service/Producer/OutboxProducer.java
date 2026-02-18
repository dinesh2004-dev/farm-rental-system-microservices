package com.example.payment_service.Producer;


import com.example.payment_service.entity.Outbox;
import com.example.payment_service.enums.OutboxStatus;
import com.example.payment_service.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OutboxProducer {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;


    private static final Logger log = LoggerFactory.getLogger(
            OutboxProducer.class);

    public OutboxProducer(OutboxRepository outboxRepository,
                                KafkaTemplate<String,String> kafkaTemplate)       {

        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;

    }

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents(){

        List<Outbox> outboxList = outboxRepository.findTop50ByStatusOrderByCreatedAt(
                OutboxStatus.PENDING
        );

        for(Outbox outbox : outboxList){


                kafkaTemplate.send(
                        resolveTopic(outbox),
                        String.valueOf(outbox.getAggregateId()),
                        outbox.getPayload()
                ).whenComplete((result,ex) ->{

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

    private String resolveTopic(Outbox outbox) {
        return switch (outbox.getEventType()) {
            case "Payment_Success" -> "payment-success-event";
            case "Payment_Failed" -> "payment-failed-event";
            default -> throw new IllegalStateException(
                    "Unknown event type " + outbox.getEventType()
            );
        };
    }


}
