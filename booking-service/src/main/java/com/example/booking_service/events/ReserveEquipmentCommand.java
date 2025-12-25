package com.example.booking_service.events;

import com.example.booking_service.entity.Booking;
import lombok.Data;

import java.time.Instant;

@Data
public class ReserveEquipmentCommand {

    private int bookingId;
    private int equipmentId;
    private Instant timestamp = Instant.now();

    public ReserveEquipmentCommand(Booking booking) {
        this.bookingId = booking.getId();
        this.equipmentId = booking.getEquipment();

    }
}
