package com.example.booking_service.consumer;

import com.example.booking_service.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.events.PaymentFailedEvent;
import org.example.events.PaymentSuccessEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class PaymentseventConsumer {

    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    public PaymentseventConsumer(BookingService bookingService,
                                      ObjectMapper objectMapper){
        this.bookingService = bookingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {"payment-success-event","payment-failed-event"},
            containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void consumePaymentSuccessEvent(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                           String payload,
                                           Acknowledgment ack){

        try{
            if("payment-success-event".equals(topic)){

                PaymentSuccessEvent paymentSuccessEvent = objectMapper.readValue(
                        payload,
                        PaymentSuccessEvent.class
                );
                bookingService.handlePaymentSuccessEvent(paymentSuccessEvent);
            }

            if("payment-failed-event".equals(topic)){


                 PaymentFailedEvent paymentFailedEvent = objectMapper.readValue(
                         payload,
                         PaymentFailedEvent.class
                 );
                 bookingService.handlePaymentFailureEvent(paymentFailedEvent);
            }

            ack.acknowledge();
        }
        catch (Exception e){
            throw new RuntimeException("Failed to process payment event",e);
        }


    }
}
