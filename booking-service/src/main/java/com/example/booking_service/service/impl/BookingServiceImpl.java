package com.example.booking_service.service.impl;

import com.example.booking_service.clients.EquipmentClient;
import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.entity.Booking;

import com.example.booking_service.enums.BookingStatus;
import com.example.booking_service.enums.PaymentStatus;
import com.example.booking_service.exceptions.BookingNotFoundException;
import com.example.booking_service.producer.ReserveEquipmentCommandProducer;
import com.example.booking_service.repository.BookingsRepository;
import com.example.booking_service.service.BookingService;
import com.example.booking_service.util.AuthUtil;
import org.example.events.PaymentFailedEvent;
import org.example.events.PaymentSuccessEvent;
import org.example.events.ReserveEquipmentCommand;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EquipmentClient equipmentClient;
    @Autowired
    private ReserveEquipmentCommandProducer reserveEquipmentCommandProducer;

    private static Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override
    @Transactional
    @PreAuthorize("hasRole('Renter')")
    public int createBooking(BookingsDTO bookingsDTO) {

        Booking booking = modelMapper.map(bookingsDTO, Booking.class);

        int renter = AuthUtil.getUserId() == null ? 0 : Integer.parseInt(Objects.requireNonNull(AuthUtil.getUserId()));
        String sagaId = UUID.randomUUID().toString();
        int days = Math.toIntExact(Duration.between(bookingsDTO.getStart_date(), bookingsDTO.getEnd_date()).toDays());
        double totalPrice = days * bookingsDTO.getPricePerDay();
        booking.setRenter(renter);
        booking.setSagaId(sagaId);
        booking.setTotalCost(totalPrice);
        booking.setBookingStatus(BookingStatus.Pending);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        bookingsRepository.save(booking);

        log.info("Booking amount : {} ", totalPrice);


        ReserveEquipmentCommand command = new ReserveEquipmentCommand(
                booking.getId(),
                bookingsDTO.getEquipment(),
                sagaId,
                Instant.now()
        );

        reserveEquipmentCommandProducer.sendReserveEquipmentCommandEvent(command);

        return booking.getId();

    }

    @Override
    public void handlePaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent) throws BookingNotFoundException {

        Booking booking = bookingsRepository.getReferenceByIdAndSagaId(paymentSuccessEvent.getBookingId(),
                paymentSuccessEvent.getSagaId());
        if(booking == null){
            log.error("Booking not found for id: {} and sagaId: {}",
                    paymentSuccessEvent.getBookingId(),
                    paymentSuccessEvent.getSagaId());
            throw new BookingNotFoundException("Booking not found for id: " + paymentSuccessEvent.getBookingId());
        }
        booking.setPaymentStatus(PaymentStatus.PAID);
        bookingsRepository.save(booking);

    }

    @Override
    public void handlePaymentFailureEvent(PaymentFailedEvent paymentFailedEvent){

        Booking booking = bookingsRepository.getReferenceByIdAndSagaId(
                paymentFailedEvent.getBookingId(),
                paymentFailedEvent.getSagaId()
        );
        if(booking == null){
            log.error("Booking not found for id: {} and sagaId: {}",
                    paymentFailedEvent.getBookingId(),
                    paymentFailedEvent.getSagaId());
            return;
        }
        booking.setPaymentStatus(PaymentStatus.FAILED);
        booking.setBookingStatus(BookingStatus.Cancelled);
        bookingsRepository.save(booking);

    }
}

