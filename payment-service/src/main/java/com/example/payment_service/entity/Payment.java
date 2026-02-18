package com.example.payment_service.entity;

import com.example.payment_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int bookingId;
    @Column(nullable = false)
    private int lenderId;
    @Column(nullable = false)
    private int payerId;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column
    private String razorpayPaymentId;
    @Column
    private String razorpayOrderId;
    @Column
    private String razorpaySignature;

    @Column(nullable = false,updatable = false,unique = true)
    private String idempotencyKey;

    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = Instant.now();
    }


}
