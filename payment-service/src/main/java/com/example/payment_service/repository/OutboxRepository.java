package com.example.payment_service.repository;

import com.example.payment_service.entity.Outbox;
import com.example.payment_service.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox,Long> {

    List<Outbox> findTop50ByStatusOrderByCreatedAt(OutboxStatus status);
}
