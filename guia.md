# ✈️ Flight Booking API

## Complete Development Guide

> **Goal:** Build a professional REST API following **Hexagonal Architecture**, using **Spring Boot**, **PostgreSQL**, **Docker**, **Swagger** and **JUnit**, similar to the architecture used in enterprise Java projects.

---

# Technologies

| Technology              | Purpose               |
| ----------------------- | --------------------- |
| Java 21                 | Programming language  |
| Spring Boot             | Backend framework     |
| Spring Web              | REST API              |
| Spring Data JPA         | Persistence           |
| PostgreSQL              | Database              |
| Docker & Docker Compose | Infrastructure        |
| Swagger (OpenAPI)       | API Documentation     |
| JUnit 5                 | Unit testing          |
| Mockito                 | Mocking               |
| Maven                   | Dependency management |
| Git                     | Version control       |

---

# Project Architecture

The project follows **Hexagonal Architecture (Ports & Adapters)**.

```
                    HTTP Request
                         │
                         ▼
                  AirportController
                         │
                         ▼
                CreateAirportUseCase
                         │
                         ▼
              AirportRepositoryPort
                         ▲
                         │
          AirportRepositoryAdapter
                         │
                         ▼
             AirportJpaRepository
                         │
                         ▼
                   PostgreSQL
```

The domain **never depends on Spring, JPA or PostgreSQL**.

---

# Project Structure

```
com.manserrub.flight_booking_api
│
├── FlightBookingApiApplication.java

├── domain                                  ← núcleo, cero dependencias de frameworks
│   ├── model
│   │   └── Airport.java
│   ├── exception
│   │   └── AirportNotFoundException.java
│   └── ports
│       ├── in
│       │   └── FindAirportUseCase.java      ← qué puede pedir el "mundo exterior"
│       └── out
│           └── AirportRepositoryPort.java    ← qué necesita el dominio del "mundo exterior"

├── application                              ← casos de uso, implementa los puertos in
│   └── service
│       └── AirportService.java              ← implements FindAirportUseCase
                                                (usa AirportRepositoryPort, inyectado)

└── infrastructure                           ← adaptadores, implementan/consumen puertos
    │
    ├── adapter
    │   ├── in
    │   │   └── web
    │   │       ├── AirportController.java    ← adaptador de entrada (usa FindAirportUseCase)
    │   │       ├── dto
    │   │       │   └── AirportResponse.java
    │   │       └── mapper
    │   │           └── AirportWebMapper.java
    │   │
    │   └── out
    │       └── persistence
    │           ├── AirportEntity.java
    │           ├── AirportJpaRepository.java
    │           ├── AirportRepositoryAdapter.java  ← implements AirportRepositoryPort
    │           └── mapper
    │               └── AirportPersistenceMapper.java
    │
    ├── exception
    │   └── GlobalExceptionHandler.java
    │
    └── config
        └── (beans de configuración si hacen falta)
```

---

# Domain

The domain contains **only business logic**.

It **must not** depend on:

* Spring
* JPA
* Hibernate
* PostgreSQL
* Swagger
* Docker

Only Java.

---

## Domain Models

### Airport

```
id
iataCode
name
city
country
```

Example:

```java
public class Airport {

    private Long id;
    private String iataCode;
    private String name;
    private String city;
    private String country;

}
```

---

### Flight

```
id
flightNumber
departureAirport
arrivalAirport
departureTime
arrivalTime
price
availableSeats
status
```

---

### Passenger

```
id
firstName
lastName
email
passportNumber
birthDate
```

---

### Booking

```
id
bookingReference
flight
passenger
bookingDate
status
seatNumber
```

---

# Ports

Ports are interfaces.

The domain only knows the interface.

Example:

```
AirportRepositoryPort
```

```java
Airport save(Airport airport);

Optional<Airport> findById(Long id);

List<Airport> findAll();

void deleteById(Long id);
```

---

# Application Layer

Contains the use cases.

Example:

```
CreateAirportUseCase
```

```java
public Airport execute(Airport airport)
```

Each use case performs **one business action only**.

Examples:

```
CreateAirportUseCase

FindAirportUseCase

DeleteAirportUseCase

UpdateAirportUseCase
```

---

# DTO

DTOs represent the data entering or leaving the API.

Example:

```
CreateAirportRequest
```

```java
private String iataCode;

private String name;

private String city;

private String country;
```

Example:

```
AirportResponse
```

```java
private Long id;

private String iataCode;

private String name;
```

---

# Infrastructure

Infrastructure contains every technology-specific implementation.

---

## Entity

Represents a database table.

Example:

```
AirportEntity
```

```java
@Entity
@Table(name="airports")
```

---

## Repository

Spring Data repository.

Example:

```
AirportJpaRepository
```

```java
extends JpaRepository<AirportEntity, Long>
```

---

## Mapper

Converts

```
Airport

↓

AirportEntity
```

and vice versa.

Example:

```
AirportMapper
```

Methods:

```
toEntity()

toDomain()
```

---

## Adapter

Implements the port.

Example:

```
AirportRepositoryAdapter
```

Responsibilities:

* Call JpaRepository
* Use Mapper
* Return Domain Model

---

## Controller

Receives HTTP requests.

Example:

```
AirportController
```

Endpoints:

```
POST /airports

GET /airports

GET /airports/{id}

PUT /airports/{id}

DELETE /airports/{id}
```

---

# Database

PostgreSQL

Table:

```
airports
```

Columns

```
id

iata_code

name

city

country
```

---

# Docker

docker-compose.yml

Contains

```
PostgreSQL

Later:

Spring Boot
```

Commands

```
docker compose up -d

docker compose down

docker compose down -v
```

---

# Swagger

URL

```
http://localhost:8080/swagger-ui/index.html
```

---

# Tests

Project should contain

```
AirportControllerTest

CreateAirportUseCaseTest

AirportRepositoryAdapterTest

AirportMapperTest
```

Goal

* Unit Tests
* Integration Tests

---

# Development Order

## Phase 1

Project setup

✔ Spring Boot

✔ Docker

✔ PostgreSQL

✔ Git

---

## Phase 2

Create Domain

```
Airport

Flight

Passenger

Booking
```

---

## Phase 3

Create Ports

```
AirportRepositoryPort

FlightRepositoryPort

PassengerRepositoryPort

BookingRepositoryPort
```

---

## Phase 4

Create Use Cases

```
CreateAirport

UpdateAirport

DeleteAirport

FindAirport
```

Repeat for every entity.

---

## Phase 5

Persistence

```
Entity

JpaRepository

Mapper

Adapter
```

---

## Phase 6

REST Controllers

Expose endpoints.

---

## Phase 7

Swagger

Document every endpoint.

---

## Phase 8

Validation

```
@NotBlank

@Email

@Positive

@Size
```

---

## Phase 9

Global Exception Handler

```
@ControllerAdvice
```

---

## Phase 10

Testing

JUnit

Mockito

---

## Phase 11

Docker

Run the complete application.

---

# Git Commit Examples

```
Initial Spring Boot project

Configure PostgreSQL

Add Airport domain model

Create AirportRepositoryPort

Implement CreateAirportUseCase

Create AirportEntity

Implement AirportJpaRepository

Create AirportMapper

Implement AirportRepositoryAdapter

Expose Airport REST endpoints

Configure Swagger

Add Airport unit tests

Containerize application
```

---

# Future Improvements

* JWT Authentication
* Role-based authorization
* Pagination
* Filtering
* Sorting
* Flight search
* Booking cancellation
* Email notifications
* Flyway migrations
* MapStruct
* GitHub Actions (CI)
* Testcontainers
* Logging with SLF4J
* API versioning
* OpenAPI annotations

---

# Final Flow

```
HTTP Request
      │
      ▼
Controller
      │
      ▼
Use Case
      │
      ▼
Repository Port
      │
      ▼
Repository Adapter
      │
      ▼
JPA Repository
      │
      ▼
PostgreSQL
      │
      ▼
Adapter
      │
      ▼
Use Case
      │
      ▼
Controller
      │
      ▼
HTTP Response
```

**Golden Rule**

* Domain knows nothing about infrastructure.
* Application orchestrates business operations.
* Infrastructure implements technology details.
* Controllers only receive and return HTTP data.
* Every layer has a single responsibility.
