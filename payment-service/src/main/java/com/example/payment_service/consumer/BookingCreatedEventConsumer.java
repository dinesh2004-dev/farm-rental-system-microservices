package com.example.payment_service.consumer;

import com.example.payment_service.repository.PaymentRepository;
import com.example.payment_service.service.PaymentService;
import org.example.events.BookingCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookingCreatedEventConsumer {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public BookingCreatedEventConsumer(PaymentService paymentService,
                                    PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @KafkaListener(topics = "booking-created-event",
            containerFactory = "bookingCreatedEventKafkaListenerContainerFactory")
    public void consumeBookingCreatedEvent(BookingCreatedEvent event, Acknowledgment ack){
        // Implement payment processing logic here

        paymentService.initiatePayment(event);

        ack.acknowledge();
    }
}
