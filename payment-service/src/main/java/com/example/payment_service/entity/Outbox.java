package com.example.payment_service.entity;

import com.example.payment_service.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.json.JSONObject;

import java.time.Instant;

@Entity
@Table(name = "outbox_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String eventType;
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
    private Instant createdAt;
    @Column
    private Instant publishedAt;

}
