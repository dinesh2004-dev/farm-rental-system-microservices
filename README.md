# 🚜 Farm Equipment Rental System – Microservices Architecture

A **scalable, event-driven Farm Equipment Rental Platform** built using **Spring Boot microservices**, enabling farmers to rent agricultural equipment seamlessly with **secure booking, payment orchestration, and fault-tolerant workflows**.

This project follows **real-world backend engineering practices** such as **Kafka-based asynchronous communication**, **Saga pattern**, **idempotency**, and **eventual consistency**.

---

## 📌 Key Features

- Secure user authentication & role-based access
- Equipment listing, search & availability management
- Booking lifecycle management using Saga pattern
- Payment processing using Razorpay
- Event-driven communication using Apache Kafka
- Fault-tolerant distributed transactions
- Dockerized microservices
- Cloud-ready (AWS compatible)

---

## 🧱 Microservices Overview

| Service | Description |
|-------|------------|
| API Gateway | Central entry point and routing |
| User Service | Authentication, authorization, user management |
| Equipment Service | Equipment listing and availability handling |
| Booking Service | Booking creation and Saga orchestration |
| Payment Service | Payment initiation, verification, refunds |
| Service Registry | Service discovery using Eureka |

---

## 🏗️ Architecture

- **Architecture Style:** Microservices
- **Communication:**
  - REST (Synchronous)
  - Kafka (Asynchronous, Event-driven)
- **Transaction Management:** Saga Pattern (Choreography-based)
- **Data Consistency:** Eventual consistency
- **Failure Handling:** Retry mechanism & compensating transactions

---

## 🔁 Saga Workflow (Booking → Payment)

1. Booking Service publishes `ReserveEquipmentCommand`
2. Equipment Service:
   - Reserves equipment
   - Publishes `EquipmentReservedEvent` or `EquipmentReservationFailedEvent`
3. Payment Service:
   - Creates Razorpay order
   - Verifies payment
   - Publishes `PaymentSuccessEvent` or `PaymentFailedEvent`
4. Booking Service:
   - Confirms booking on success
   - Cancels booking on failure

✔️ Prevents partial failures in distributed systems

---

## 🧰 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- Spring Cloud Gateway
- Eureka Discovery Server

### Messaging
- Apache Kafka
- Kafka Producers & Consumers
- Idempotent event processing

### Database
- MySQL (Database per service)

### Payments
- Razorpay Payment Gateway

### DevOps
- Docker & Docker Compose
- AWS EC2 (deployment-ready)

---

## 📂 Project Structure

