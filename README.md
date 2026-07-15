# Flight Booking API

A backend service for managing flight booking data, built with **Java**, **Spring Boot**, and **PostgreSQL**, following **Hexagonal Architecture** (Ports & Adapters) principles.

This project demonstrates a clean separation between business logic and infrastructure concerns, making the codebase testable, framework-independent at its core, and easy to extend with new adapters (REST, messaging, different persistence technologies, etc.) without touching the domain logic.

## Table of Contents

- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Roadmap](#roadmap)

## Architecture

The project follows **Hexagonal Architecture (Ports & Adapters)**, structured in three main layers:

- **Domain**: pure business logic and models, with zero dependencies on frameworks (no Spring, no JPA annotations). Defines the contracts (ports) the application needs.
- **Application**: implements the use cases defined by the domain's inbound ports, orchestrating calls to outbound ports.
- **Infrastructure**: adapters that connect the application to the outside world — REST controllers (inbound) and JPA/PostgreSQL persistence (outbound) — implementing the domain's ports without the domain ever depending on them.

```
domain
 ├── model            → business entities (e.g. Airport)
 ├── exception         → domain-specific exceptions
 └── ports
     ├── in            → use case interfaces (what the app can do)
     └── out           → repository interfaces (what the domain needs)

application
 └── service           → use case implementations

infrastructure
 ├── adapter
 │   ├── in            → REST controllers, request/response DTOs, web mappers
 │   └── out           → JPA entities, Spring Data repositories, persistence mappers
 ├── config            → application configuration
 └── exception         → global exception handling (REST error responses)
```

This design keeps the **domain independent of frameworks**, so the business rules can be tested in isolation and the persistence or delivery mechanism (e.g. swapping PostgreSQL for MongoDB, or REST for GraphQL) can change without impacting core logic.

## Tech Stack

- **Java 17+**
- **Spring Boot** (Web, Data JPA)
- **PostgreSQL** (containerized with Docker)
- **Hibernate** as JPA provider
- **Maven** (with Maven Wrapper included)
- **Docker & Docker Compose** for local database provisioning

## Project Structure

```
com.manserrub.flight_booking_api
├── domain
│   ├── model
│   ├── exception
│   └── ports
│       ├── in
│       └── out
├── application
│   └── service
└── infrastructure
    ├── adapter
    │   ├── in
    │   │   ├── controller
    │   │   ├── dto
    │   │   └── mapper
    │   └── out
    │       ├── entity
    │       ├── repository
    │       └── mapper
    ├── config
    └── exception
```

## Getting Started

### Prerequisites

- JDK 17 or higher
- Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/flight-booking-api.git
cd flight-booking-api
```

### 2. Start the database

```bash
docker compose up -d
```

This spins up a PostgreSQL container with the database `flight_booking`, ready for the application to connect to.

### 3. Run the application

Using the included Maven Wrapper (no local Maven installation required):

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
.\mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint                          | Description                        |
|--------|------------------------------------|-------------------------------------|
| GET    | `/api/airports`                   | List all airports                   |
| GET    | `/api/airports/{id}`              | Get an airport by its ID            |
| GET    | `/api/airports/iata/{iataCode}`   | Get an airport by its IATA code     |
| GET    | `/api/airports/search?city=...`   | Search airports by city             |
| GET    | `/api/airports/search?country=...`| Search airports by country          |

### Example response

```json
{
  "id": 1,
  "iataCode": "MAD",
  "name": "Adolfo Suárez Madrid-Barajas",
  "city": "Madrid",
  "country": "España"
}
```

## Running Tests

```bash
./mvnw test
```

## Roadmap

- [ ] Write operations (create, update, delete airports)
- [ ] Database migrations with Flyway
- [ ] Unit and integration test coverage
- [ ] API documentation with OpenAPI/Swagger
- [ ] Additional domains (flights, bookings, users) following the same hexagonal approach
- [ ] Authentication & authorization

## License

This project is for educational and portfolio purposes.
