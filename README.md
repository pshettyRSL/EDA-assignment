# Order & Notification System (EDA)

An Event-Driven Architecture (EDA) application built with **Spring Boot** and **Apache Kafka**, demonstrating decoupled event publishing, topic partitioning, parallel consumer processing, and idempotent consumption.

---

### How an event flows through the system:

#### 1. Order Placed Flow:
- **Event Creation (Producer side)**: A client sends a `POST /api/orders/` request with order details. The `OrderController` receives the payload and passes it to `OrderProducerService`, which wraps it in an immutable `OrderPlacedEvent` containing a unique `eventId` (UUID), `eventType` ("OrderPlaced"), `timestamp` (UTC), and the `OrderPayload`.
- **Event Publishing**: The `OrderProducerService` publishes the event to the Kafka topic `order-placed-topic` using `KafkaTemplate.send()`. The `orderId` is used as the message key to ensure partition-level ordering for the same order.
- **Event Brokering (Kafka)**: Kafka persists the message across 3 partitions. Two consumer groups are independently subscribed, each maintaining their own offset. This is the Pub/Sub pattern i.e. every consumer group receives every message.
- **Event Consumption — Notification Service**: The `NotificationConsumer` (group: `notification-consumer-group`) receives the event and simulates sending an email confirmation. It processes the event and logs the action.
- **Event Consumption — Analytics Service**: The `AnalyticsConsumer` (group: `analytics-consumer-group`) receives the same event independently. Before processing, it checks its idempotency store (`processed_events` table) for the `eventId`. If found, it skips processing entirely. If not found, it processes the event and records the `eventId` to prevent future duplicates.

#### 2. User Registration Flow (Extension):
- **Event Creation**: A client sends a `POST /api/users/register` request with email and password (`UserDTO`). The `UserController` receives the payload and creates a `UserRegisteredEvent` (auto-generated UUID, eventType `"UserRegistered"`, timestamp, and user email).
- **Event Publishing**: The `UserProducerService` publishes the event to the Kafka topic `user.registered.topic` using the user's email as the message key for partition-level ordering.
- **Event Consumption — Email Service**: The `EmailConsumer` (group: `email-consumer-group`) listens on `user.registered.topic` and simulates sending a welcome email to the newly registered user.
- **Event Consumption — Audit Service**: The `AuditConsumer` (group: `audit-consumer-group`) listens independently on `user.registered.topic` and logs an audit trail of the user registration event.

---

## How to Run the Application

### 1. Prerequisites
* **Java 17+**
* **Docker & Docker Compose** (for running Kafka)
* **Maven** (or included `mvnw` / `mvnw.cmd`)

### 2. Start Apache Kafka
Start the Kafka broker using Docker Compose:
```bash
docker compose up -d
```

### 3. Build & Run the Spring Boot Application
In the project directory:

* **Windows (PowerShell or CMD)**:
  ```powershell
  .\mvnw.cmd clean spring-boot:run
  ```

* **Linux / macOS**:
  ```bash
  ./mvnw clean spring-boot:run
  ```

The server will start listening on port **`8080`**.

---

## API Documentation & Triggering (cURL)

The base URL is `http://localhost:8080`.

### 1. User Registration API
Publishes a user registration event to Kafka topic `user.registered.topic`.

* **Endpoint**: `POST /api/users/register`
* **Content-Type**: `application/json`
* **Request Body**:
  ```json
  {
    "email": "xyz@gmail.com",
    "password": "test123"
  }
  ```

#### cURL Commands:

* **Windows PowerShell**:
  ```powershell
  curl.exe http://localhost:8080/api/users/register --json '{\"email\": \"xyz@gmail.com\", \"password\": \"test123\"}'
  ```

* **Windows Command Prompt (CMD)**:
  ```cmd
  curl.exe -s -X POST http://localhost:8080/api/users/register -H "Content-Type: application/json" -d "{\"email\": \"xyz@gmail.com\", \"password\": \"test123\"}"
  ```

* **Linux / macOS / Git Bash**:
  ```bash
  curl -s -X POST http://localhost:8080/api/users/register \
    -H "Content-Type: application/json" \
    -d '{"email": "xyz@gmail.com", "password": "test123"}'
  ```

* **Expected Response**:
  ```text
  HTTP/1.1 200 OK
  User registered successfully
  ```

---

### 2. Place Order API
Publishes an order placed event to Kafka topic `order-placed-topic`.

* **Endpoint**: `POST /api/orders/`
* **Content-Type**: `application/json`
* **Request Body**:
  ```json
  {
    "orderId": "ORD-1001",
    "customerId": "CUST-987",
    "customerEmail": "xyz@gmail.com",
    "totalAmount": 149.99,
    "items": [
      "Wireless Keyboard",
      "Ergonomic Mouse"
    ]
  }
  ```

#### cURL Commands:

* **Windows PowerShell**:
  ```powershell
  Invoke-RestMethod -Uri "http://localhost:8080/api/orders/" `
    -Method Post `
    -ContentType "application/json" `
    -Body '{"orderId":"ORD-1001","customerId":"CUST-987","customerEmail":"xyz@gmail.com","totalAmount":149.99,"items":["Wireless Keyboard","Ergonomic Mouse"]}'
  ```

* **Linux / macOS / Git Bash**:
  ```bash
  curl -s -X POST http://localhost:8080/api/orders/ \
    -H "Content-Type: application/json" \
    -d '{
      "orderId": "ORD-1001",
      "customerId": "CUST-987",
      "customerEmail": "xyz@gmail.com",
      "totalAmount": 149.99,
      "items": ["Wireless Keyboard", "Ergonomic Mouse"]
    }'
  ```

* **Expected Response**:
  Returns the created `OrderPlacedEvent` JSON with generated `eventId` and `timestamp`.

---

### 3. Publish Duplicate Event (Idempotency Testing)
Re-publishes an already processed event to verify that the analytics consumer skips duplicate processing.

* **Endpoint**: `POST /api/orders/publish-duplicate-event`
* **Content-Type**: `application/json`
* **Request Body**: Pass the full `OrderPlacedEvent` JSON received from placing the order.
