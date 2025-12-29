package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor                 // REQUIRED for Jackson
@AllArgsConstructor                // Convenience for producer
public class ReserveEquipmentCommand {

    private int bookingId;
    private int equipmentId;
    private String sagaId;
    private Instant timestamp;
}
