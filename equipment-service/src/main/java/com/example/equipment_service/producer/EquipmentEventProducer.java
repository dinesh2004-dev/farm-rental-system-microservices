package com.example.equipment_service.producer;

import org.example.events.EquipmentReservationFailedEvent;
import org.example.events.EquipmentReservedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EquipmentEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;


    public EquipmentEventProducer(KafkaTemplate<String,Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    public void sendReserveEquipmentSuccessEvent(EquipmentReservedEvent event){

        kafkaTemplate.send(
                "equipment-reserved-event",
                String.valueOf(event.getBookingId()),
                event
        );
    }

    public void sendReserveEquipmentFailedEvent(EquipmentReservationFailedEvent event){

        kafkaTemplate.send(
                "equipment-reservation-failed-event",
                String.valueOf(event.getBookingId()),
                event
        );
    }
}
