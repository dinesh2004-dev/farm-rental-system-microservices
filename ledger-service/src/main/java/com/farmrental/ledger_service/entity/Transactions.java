package com.farmrental.ledger_service.entity;

import com.farmrental.ledger_service.enums.ReferenceType;
import com.farmrental.ledger_service.enums.TransactionStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "transactions",
    uniqueConstraints = @UniqueConstraint(
            columnNames = {"referenceId","referenceType"}
    )
)
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType referenceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private Long totalAmount;

    @Column
    private Instant processedAt;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }

}
