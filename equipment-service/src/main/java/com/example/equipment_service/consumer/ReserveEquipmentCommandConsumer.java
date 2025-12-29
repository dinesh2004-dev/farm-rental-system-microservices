package com.example.equipment_service.consumer;

import com.example.equipment_service.producer.EquipmentEventProducer;
import com.example.equipment_service.service.EquipmentService;
import org.example.events.EquipmentReservationFailedEvent;
import org.example.events.EquipmentReservedEvent;
import org.example.events.ReserveEquipmentCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReserveEquipmentCommandConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(ReserveEquipmentCommandConsumer.class);

    private final EquipmentService equipmentService;
    private final EquipmentEventProducer equipmentEventProducer;

    public ReserveEquipmentCommandConsumer(
            EquipmentService equipmentService,
            EquipmentEventProducer equipmentEventProducer
    ) {
        this.equipmentService = equipmentService;
        this.equipmentEventProducer = equipmentEventProducer;
    }

    @KafkaListener(
            topics = "reserve-equipment-command",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            ReserveEquipmentCommand command,
            Acknowledgment ack
    ) {
        log.info(
                "ReserveEquipmentCommand received | bookingId={} equipmentId={} sagaId={}",
                command.getBookingId(),
                command.getEquipmentId(),
                command.getSagaId()
        );

        try {
            boolean reserved =
                    equipmentService.reserveEquipment(command.getEquipmentId());

            if (reserved) {
                equipmentEventProducer.sendReserveEquipmentSuccessEvent(
                        new EquipmentReservedEvent(
                                command.getBookingId(),
                                command.getEquipmentId(),
                                command.getSagaId(),
                                Instant.now()
                        )
                );
            } else {
                equipmentEventProducer.sendReserveEquipmentFailedEvent(
                        new EquipmentReservationFailedEvent(
                                command.getBookingId(),
                                command.getEquipmentId(),
                                command.getSagaId(),
                                "Equipment not available",
                                Instant.now()
                        )
                );
            }


            ack.acknowledge();

        } catch (Exception ex) {
            log.error(
                    "Infrastructure failure while reserving equipmentId={}",
                    command.getEquipmentId(),
                    ex
            );

        }
    }
}
