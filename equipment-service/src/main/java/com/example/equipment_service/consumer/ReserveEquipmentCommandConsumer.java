package com.example.equipment_service.consumer;

import com.example.equipment_service.Repository.EquipmentRepository;
import com.example.equipment_service.entity.Equipment;
import com.example.equipment_service.enums.EventType;
import com.example.equipment_service.producer.EquipmentEventProducer;
import com.example.equipment_service.service.EquipmentService;
import com.example.equipment_service.util.OutboxUtil;
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
//    private final EquipmentEventProducer equipmentEventProducer;
    private final EquipmentRepository equipmentRepository;
    private final OutboxUtil outboxUtil;

    public ReserveEquipmentCommandConsumer(
            EquipmentService equipmentService,
            EquipmentEventProducer equipmentEventProducer,
            EquipmentRepository equipmentRepository,
            OutboxUtil outboxUtil
    ) {
        this.equipmentService = equipmentService;
//        this.equipmentEventProducer = equipmentEventProducer;
        this.equipmentRepository = equipmentRepository;
        this.outboxUtil = outboxUtil;
    }

    @KafkaListener(
            topics = "reserve-equipment-command",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            ReserveEquipmentCommand command,
            Acknowledgment ack
    ) {




        try {

            boolean reserved =
                    equipmentService.reserveEquipment(command.getEquipmentId());

            Equipment equipment =
                    equipmentService.getEquipmentById(command.getEquipmentId());

            equipment.setSagaId(command.getSagaId());

            equipmentRepository.save(equipment);
            if (reserved) {

                log.info(
                        "ReserveEquipmentCommand received | bookingId={} equipmentId={} Lender={} sagaId={}",
                        command.getBookingId(),
                        command.getEquipmentId(),
                        equipment.getOwnerId(),
                        command.getSagaId()
                );
//                equipmentEventProducer.sendReserveEquipmentSuccessEvent(
//                        new EquipmentReservedEvent(
//                                command.getBookingId(),
//                                command.getEquipmentId(),
//                                equipment.getOwnerId(),
//                                command.getSagaId(),
//                                Instant.now()
//                        )
//                );

                outboxUtil.publishOutboxEvents(EventType.EQUIPMENT_RESERVED,
                        equipment,
                        new EquipmentReservedEvent(
                                command.getBookingId(),
                                command.getEquipmentId(),
                                equipment.getOwnerId(),
                                command.getSagaId(),
                                Instant.now()
                        ));

            } else {

//                equipmentEventProducer.sendReserveEquipmentFailedEvent(
//                        new EquipmentReservationFailedEvent(
//                                command.getBookingId(),
//                                command.getEquipmentId(),
//                                command.getSagaId(),
//                                "Equipment not available",
//                                Instant.now()
//                        )
//                );

                outboxUtil.publishOutboxEvents(EventType.EQUIPMENT_RESERVATION_FAILED,
                        equipment,
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
