package com.example.booking_service.service.impl;

import com.example.booking_service.clients.EquipmentClient;
import com.example.booking_service.dtos.BookingsDTO;
import com.example.booking_service.dtos.EquipmentInfo;
import com.example.booking_service.entity.Booking;
import com.example.booking_service.repository.BookingsRepository;
import com.example.booking_service.service.BookingService;
import com.example.booking_service.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.util.Objects;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired

    private EquipmentClient equipmentClient;


    @Override
    @PreAuthorize("hasRole('Renter')")
    public int createBooking(BookingsDTO bookingsDTO) {

        int equipment = bookingsDTO.getEquipment();

        EquipmentInfo equipmentInfo = equipmentClient.getEquipmentById(equipment);

        if(Objects.isNull(equipmentInfo)){

            throw new RuntimeException("Equipment not found with id: " + equipment);
        }

        if(!equipmentInfo.isAvailable()){

            throw new RuntimeException("Equipment with id: " + equipment + " is not available for booking");
        }
        if(bookingsRepository.existsOverlappingBooking(equipment,
                bookingsDTO.getStart_date(), bookingsDTO.getEnd_date())){

            throw new RuntimeException("Equipment with id: " + equipment + " is already booked for the selected dates");
        }
        Booking booking = modelMapper.map(bookingsDTO, Booking.class);
        booking.setLender(equipmentInfo.getOwnerId());
        int renterId = Integer.parseInt(Objects.requireNonNull(AuthUtil.getUserId()));
        booking.setRenter(renterId);
        Booking savedBooking = bookingsRepository.save(booking);

        return savedBooking.getId();

        
    }
}
