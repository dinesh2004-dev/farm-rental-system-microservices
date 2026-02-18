package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {

    private int bookingId;
    private int paymentId;
    private String sagaId;
    private String reason;
    private Instant createdAt;
}
