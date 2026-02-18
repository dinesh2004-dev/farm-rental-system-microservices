package com.example.equipment_service.entity;

import com.example.equipment_service.enums.EventType;
import com.example.equipment_service.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name="outbox_event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
    @Column(nullable = false,unique = true)
    private String eventId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false,columnDefinition = "jsonb")
    private String payload;
    @Column(nullable = false)
    private String aggregateType;
    @Column(nullable = false)
    private long aggregateId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;
    @Column(nullable = false)
    private String sagaId;
    @Column(nullable = false)
    private Instant createdAt;
    @Column
    private Instant publishedAt;
}
