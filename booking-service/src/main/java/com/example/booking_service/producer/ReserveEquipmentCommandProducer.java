package com.example.booking_service.producer;

import org.example.events.ReserveEquipmentCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReserveEquipmentCommandProducer {

    private final KafkaTemplate<String,ReserveEquipmentCommand> kafkaTemplate;

    public ReserveEquipmentCommandProducer(KafkaTemplate<String, ReserveEquipmentCommand> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReserveEquipmentCommandEvent(ReserveEquipmentCommand command){
        kafkaTemplate.send("reserve-equipment-command",String.valueOf(
                        command.getEquipmentId()),
                command);
    }
}


