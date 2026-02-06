package com.example.payment_service.Producer;

import org.example.events.PaymentFailedEvent;
import org.example.events.PaymentSuccessEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publistPaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent){

        kafkaTemplate.send(
                "payment-success-event",
                String.valueOf(paymentSuccessEvent.getBookingId()),
                paymentSuccessEvent
        );
    }

    public void publishPaymentFailedEvent(PaymentFailedEvent paymentFailedEvent){

        kafkaTemplate.send(
                "payment-Failed-event",
                String.valueOf(paymentFailedEvent.getBookingId()),
                paymentFailedEvent
        );
    }
}
