package com.example.booking_service.producer;

import org.example.events.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class BookingCreatedEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public BookingCreatedEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingCreatedEvent(BookingCreatedEvent event){
        kafkaTemplate.send("booking-created-event",
                String.valueOf(event.getBookingId()),
                event);
    }
}
