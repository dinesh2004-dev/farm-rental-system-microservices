package com.example.equipment_service.producer;

import com.example.equipment_service.events.EquipmentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EquipmentEventProducer {

    private final KafkaTemplate<String, EquipmentEvent> kafkaTemplate;

    public EquipmentEventProducer(KafkaTemplate<String, EquipmentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEquipmentEvent(EquipmentEvent event){

        kafkaTemplate.send("equipment-added-topic",String.valueOf(event.getId()),event);
    }
}
