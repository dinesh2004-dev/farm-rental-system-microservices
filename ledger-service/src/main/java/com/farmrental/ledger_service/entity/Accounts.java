package com.farmrental.ledger_service.entity;

import com.farmrental.ledger_service.enums.AccountCode;
import com.farmrental.ledger_service.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"ownerId", "accountCode"}
        )
)
@Data
public class Accounts{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;   // 0 = system account

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountCode accountCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
    }
}