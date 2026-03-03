package com.farmrental.ledger_service.entity;

import com.farmrental.ledger_service.enums.EntryType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transaction;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Accounts account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate(){

        this.createdAt = Instant.now();
    }

}
