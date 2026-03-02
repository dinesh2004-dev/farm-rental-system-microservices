package com.example.payment_service.entity;

import com.example.payment_service.enums.WebhookStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "webhook_events")
@NoArgsConstructor
@Data
public class WebhookEvent {

    @Id
    private String eventId;
    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @Column(nullable = false,updatable = false)
    private String entityId;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String payload;
    @Column
    private Instant processedAt;
    @Column(nullable = false)
    private String eventType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebhookStatus status;



    @PrePersist
    public void onCreate(){

        this.createdAt = Instant.now();
    }
}
