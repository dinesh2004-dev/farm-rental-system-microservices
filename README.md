# 🌾 Farm Equipment Rental System – Microservices Architecture

## 📌 Overview

Farm Equipment Rental System is a backend application built using **Java and Spring Boot**, designed using a **Microservices Architecture**.

The platform connects:

- **Lenders** – Farmers who own equipment and want to rent it out  
- **Renters** – Farmers who need equipment  
- **Admin** – Manages users and system-level operations  

The system was migrated from a monolithic architecture to microservices to improve scalability, modularity, and service independence.

---

## 🏗 Architecture Overview

This project follows a distributed microservices architecture with:

- **Eureka Server** – Service Discovery
- **API Gateway** – Centralized routing & authentication
- **Apache Kafka** – Event-driven communication
- **Saga Pattern (Choreography-based)** – Distributed transaction handling

All external client requests pass through the **API Gateway**, which routes them to appropriate services.

---

## 🔧 Microservices

### 1️⃣ Eureka Server
- Registers all microservices
- Enables dynamic service discovery

---

### 2️⃣ API Gateway
- Single entry point for all client requests
- Routes requests to respective services
- Handles authentication and request filtering

---

### 3️⃣ User Service

Manages all user-related operations.

**User Roles:**
- Lender
- Renter
- Admin

**Features:**
- Add user
- Delete user
- Fetch user details
- Role-based access handling

---

### 4️⃣ Equipment Service

Handles equipment-related operations.

**Lenders can:**
- Add equipment
- Update equipment
- Delete equipment

**Features:**
- Equipment listing
