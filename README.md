# 🌾 Farm Equipment Rental System – Microservices Architecture

## 📌 Overview

Farm Equipment Rental System is a backend application built using **Java and Spring Boot**, designed with a **Microservices Architecture**.

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
- **JWT-based Role Authentication** – Secure access control

All client requests pass through the **API Gateway**, where authentication and authorization are enforced before routing to respective services.

---

## 🔐 Authentication & Authorization

- Implemented **JWT (JSON Web Token)** based authentication.
- Users receive a JWT token upon successful login.
- API Gateway validates the token before forwarding requests.
- Role-based access control is enforced for:
  - Lender
  - Renter
  - Admin
- Only authorized roles can access specific endpoints.

---

## 🔧 Microservices

### 1️⃣ Eureka Server
- Registers all microservices
- Enables dynamic service discovery

---

### 2️⃣ API Gateway
- Single entry point for all client requests
- Validates JWT tokens
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
- User registration
- Login & JWT generation
- Add/Delete users (Admin)
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
- Equipment search based on:
  - Radius
  - Price
  - Location

Access restricted based on user role.

---

### 5️⃣ Booking Service

Handles equipment booking operations.

**Renters can:**
- Book equipment
- View booking details

Coordinates with Payment and Equipment services during booking workflow.

---

### 6️⃣ Payment Service

- Integrated with **Razorpay API**
- Uses **Razorpay Test Mode** (for development purposes)
- Handles payment creation and verification
- Triggered during booking process

---

## 🔁 Distributed Transaction Management

The booking workflow involves multiple services:

- Booking Service
- Payment Service
- Equipment Service

To maintain data consistency across services:

- Implemented **Saga Pattern (Choreography-based)**
- Used **Apache Kafka** for publishing and consuming events

If any step fails (e.g., payment failure), compensating events are triggered to maintain system consistency.

---

## 🔄 Booking Workflow

1. Renter sends booking request via API Gateway  
2. Booking Service creates booking event  
3. Payment Service processes payment (Razorpay test mode)  
4. On successful payment:
   - Equipment availability is updated  
5. On failure:
   - Compensation event is triggered  
   - Booking is rolled back  

---

## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Cloud Gateway
- Spring Security
- JWT Authentication
- Eureka Server
- Apache Kafka
- PostgreSQL
- JPA / Hibernate
- Razorpay API (Test Mode)

---

## ▶️ How to Run the Project

### Prerequisites

- Java 17+
- Maven
- PostgreSQL
- Apache Kafka

### Steps

1. Start PostgreSQL and create required databases for each service.
2. Start Kafka server.
3. Start Eureka Server.
4. Start other services in this order:
   - API Gateway
   - User Service
   - Equipment Service
   - Booking Service
   - Payment Service
5. Access APIs via API Gateway.

---

## 📈 Key Concepts Implemented

- Microservices Architecture
- Service Discovery
- API Gateway Pattern
- JWT-based Role Authentication
- Event-Driven Architecture
- Saga Pattern for Distributed Transactions
- Third-Party Payment Integration
- Role-Based System Design

---

## 🚀 Future Enhancements

- Centralized logging
- Circuit breaker implementation (Resilience4j)
- Docker containerization
- Cloud deployment
- Monitoring & tracing (Zipkin / Sleuth)

---

## 👨‍💻 Author

**Dinesh Reddy Donthireddy**  
LinkedIn: https://www.linkedin.com/in/dinesh-reddy-b079ba27b/  
GitHub: https://github.com/dinesh2004-dev
