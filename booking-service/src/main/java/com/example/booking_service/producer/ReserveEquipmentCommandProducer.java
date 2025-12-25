package com.example.booking_service.producer;

import com.example.booking_service.events.ReserveEquipmentCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class sendReserveEquipmentCommand {

    private final KafkaTemplate<String,ReserveEquipmentCommand> kafkaTemplate;

    public sendReserveEquipmentCommand(KafkaTemplate<String,ReserveEquipmentCommand> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReserveEquipmentCommandEvent(ReserveEquipmentCommand command){
        kafkaTemplate.send("reserve-equipment-command",String.valueOf(
                command.getBookingId()),
                command);
    }
}
