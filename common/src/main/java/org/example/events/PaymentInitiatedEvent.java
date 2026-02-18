package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiatedEvent {

    public int bookingId;
    public String sagaId;
    public Instant createdAt;

}
