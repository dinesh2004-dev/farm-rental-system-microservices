package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {

    public int bookingId;
    public int RenterId;
    public int LenderId;
    public int equipmentId;
    public double amount;
    public String sagaId;
}
