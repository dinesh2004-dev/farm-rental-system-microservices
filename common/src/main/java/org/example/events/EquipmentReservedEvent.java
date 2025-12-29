package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReservedEvent {
    private int bookingId;
    private int equipmentId;
    private String sagaId;
    private Instant timestamp;
}
