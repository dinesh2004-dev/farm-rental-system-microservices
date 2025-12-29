package com.example.booking_service.consumer;

import com.example.booking_service.entity.Booking;
import com.example.booking_service.enums.BookingStatus;
import com.example.booking_service.repository.BookingsRepository;
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

    @Autowired
    private BookingsRepository bookingsRepository;

    @Transactional
    @KafkaListener(topics = "equipment-reserved-event",
                        containerFactory = "equipmentReservedEventKafkaListenerContainerFactory")
    public void consumeReservedEquipmentEvent(EquipmentReservedEvent equipmentReservedEvent,
                                               Acknowledgment ack){

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
        } else {
            existingBooking.setBookingStatus(BookingStatus.Approved);
            bookingsRepository.save(existingBooking);
            ack.acknowledge();
        }


    }

    @Transactional
    @KafkaListener(topics = "equipment-reservation-failed-event",
            containerFactory = "equipmentReservationFailedEventKafkaListenerContainerFactory")
    public void consumeReservedEquipmentFailedEvent(EquipmentReservationFailedEvent equipmentReservationFailedEvent,
                                                    Acknowledgment ack){

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

        }

    }
}
