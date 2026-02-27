package com.example.payment_service.repository;

import com.example.payment_service.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent,String> {


}
