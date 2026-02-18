package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {

    private int bookingId;
    private int paymentId;
    private String sagaId;
    private Instant createdAt;

}
