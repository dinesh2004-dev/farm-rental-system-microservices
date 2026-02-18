package com.example.booking_service.consumer;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.enums.BookingStatus;
import com.example.booking_service.producer.BookingCreatedEventProducer;
import com.example.booking_service.repository.BookingsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.events.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.annotation.Transactional;
import org.example.events.EquipmentReservationFailedEvent;
import org.example.events.EquipmentReservedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentReservationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(
                                EquipmentReservationEventConsumer.class);

    private final BookingsRepository bookingsRepository;

    private final BookingCreatedEventProducer bookingCreatedEventProducer;

    private final ObjectMapper objectMapper;

    public EquipmentReservationEventConsumer(BookingsRepository bookingsRepository,
                                             BookingCreatedEventProducer bookingCreatedEventProducer,
                                             ObjectMapper objectMapper) {
        this.bookingsRepository = bookingsRepository;
        this.bookingCreatedEventProducer = bookingCreatedEventProducer;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "equipment-reserved-event",
                        containerFactory = "stringKafkaListenerContainerFactory")
    public void consumeReservedEquipmentEvent(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                               String payload,
                                               Acknowledgment ack){

        try{
            EquipmentReservedEvent equipmentReservedEvent = objectMapper.readValue(
                    payload, EquipmentReservedEvent.class
            );

            int bookingId = equipmentReservedEvent.getBookingId();
            Optional<Booking> booking = bookingsRepository.findById(bookingId);

            if (booking.isEmpty()) {
                ack.acknowledge();
                return;
            }


            Booking existingBooking = booking.get();

            if(existingBooking.getBookingStatus() != BookingStatus.Pending){
                ack.acknowledge();
                return;
            }

            existingBooking.setBookingStatus(BookingStatus.Approved);
            bookingsRepository.save(existingBooking);

            bookingCreatedEventProducer.sendBookingCreatedEvent(
                    new BookingCreatedEvent(
                            existingBooking.getId(),
                            existingBooking.getRenter(),
                            equipmentReservedEvent.getLenderId(),
                            existingBooking.getEquipment(),
                            existingBooking.getTotalCost(),
                            equipmentReservedEvent.getSagaId()

                    )
            );

            log.info("Booking with id {} has been approved.", existingBooking.getId());
            ack.acknowledge();

        }
        catch (Exception e){

            log.error("Poison message detected, moving to DLT. payload={}", payload, e);
            ack.acknowledge();
        }


    }

    @Transactional
    @KafkaListener(topics = "equipment-reservation-failed-event",
            containerFactory = "stringKafkaListenerContainerFactory")
    public void consumeReservedEquipmentFailedEvent(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                                    String payload,
                                                    Acknowledgment ack){

        try{
            EquipmentReservationFailedEvent equipmentReservationFailedEvent = objectMapper.readValue(
                    payload, EquipmentReservationFailedEvent.class
            );
            int bookingId = equipmentReservationFailedEvent.getBookingId();
            Optional<Booking> booking = bookingsRepository.findById(bookingId);

            if (booking.isEmpty()) {
                ack.acknowledge();
                return;
            }


            Booking existingBooking = booking.get();

            if(existingBooking.getBookingStatus() != BookingStatus.Pending){
                ack.acknowledge();
                return;
            } else {
                existingBooking.setBookingStatus(BookingStatus.Cancelled);
                bookingsRepository.save(existingBooking);
                ack.acknowledge();
                log.info(equipmentReservationFailedEvent.getReason());
            }
        }
        catch (Exception e){

            log.error("Poison message detected, moving to DLT. payload={}", payload, e);
            ack.acknowledge();
        }

    }
}
