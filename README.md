# SalsaTest Backend - Technical Test

This project is an implementation of the backend technical test for **Salsa.dev**, developed using **Java 21** and **Spring Boot 3**, following a **clean architecture inspired by hexagonal principles**.

---

## 💡 Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA + H2 Database
- Spring Security + JWT
- Redis (for rate limiting)
- Lombok
- Maven

---

## 🏢 Chosen Architecture

A **clean architecture inspired by hexagonal design** has been used. The goal was to keep the code modular and testable, without introducing too much complexity for an MVP. While the classic port/adapter structure was simplified, adopting a full hexagonal architecture in the future would be straightforward.

```
com.salsa.test.salsa
├── domain
│   ├── model          -> Pure domain classes
│   └── service        -> Domain services
├── infra
│   ├── persistence    -> JPA repositories
│   ├── rest           -> REST controllers
│   ├── security       -> JWT + filters
│   └── entity         -> JPA entities
├── config             -> Configuration
└── SalsaTestApplication.java
```

---

## 🛠️ Main Components

### 🔐 Security
- JWT-based authentication
- Users from H2
- Redis-based rate limiting
- H2 Console only in dev

### 💼 Job Offers
- Basic CRUD + search by:
  - Title
  - Location
  - Salary → `maxSalary >= salary`

### 📊 Ranking
- Based on rules:
  - Recent offers (< 7 days)
  - Higher salary
  - More open jobs
- Implemented using the **Strategy pattern**, making rules reorderable and extendable via config

### 📄 Pagination
- Built-in pagination support
- `?page=0&size=5&sort=createdAt,desc`

---

## ✅ Main Endpoints

### 🔐 Authentication
```http
POST /auth/login
{
  "username": "empleador1",
  "password": "password"
}
```

### 💼 Job Offers
```http
GET /api/job-offers
Params:
  - title, location, salary
  - page, size, sort
```

```http
GET /api/job-offers/{id}
```

```http
POST /api/job-offers
Content-Type: application/json
Body:
{
  "title": "Backend Dev",
  "companyId": "uuid",
  "companyName": "Salsa",
  "description": "Job description",
  "jobType": "FULL_TIME",
  "salaryMin": 1000,
  "salaryMax": 2000,
  "benefits": [],
  "extras": []
}
```

---

## 📁 Sample Data

Preloaded via `data.sql` (for MVP only):

| username    | password  | role      |
|-------------|-----------|-----------|
| empleador1  | password  | EMPLOYER  |
| empleado1   | password  | EMPLOYEE  |

> In a production system, something like **Flyway** or **Liquibase** should be used for migrations.

---

## 📦 Running the Project

### Requirements
- Java 21
- Docker (for Redis)

### 1. Start Redis
```bash
docker-compose up -d redis
```

### 2. Start the App
```bash
./mvnw spring-boot:run
```

### 3. Access H2 Console
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:jobsdb
User: sa
Password:
```

---

## 🧰 Tests

### Run All Tests
```bash
./mvnw test
```

- Unit and integration tests
- Coverage for login, filters, ranking, controllers

---

## ✏️ Future Extensions

- Job type filter
- Benefits filter
- DB-configurable rule priority
- Redis or RabbitMQ workers for caching

---

## 📋 Design Clarifications

- Used a **clean architecture inspired by hexagonal**. Avoided full port/adapter complexity for MVP simplicity.
- **Ranking** is implemented with the **Strategy pattern**. Rules are easy to add and reorder via config.
- Salary filtering uses `maxSalary >= salary`. Example: `salary=1500` matches offers with up to `maxSalary=2000`.
- Global error handler (`@ControllerAdvice`) was not included due to time limits.
- Preloaded data with `data.sql` for demo. In future, **Liquibase** or **Flyway** would be preferred.
- Background workers were not implemented, but plan is to cache rankings post-creation for fast searches.
- System is designed for scalability: pagination, stateless JWT auth, Redis rate limiting, modular logic.

---

## 🎉 Author

Developed by Marcos Rojas as part of the technical test for Salsa.dev

---

# SalsaTest Backend - Prueba Técnica

Este proyecto es una implementación de la prueba técnica de backend para **Salsa.dev**, desarrollada en **Java 21** con **Spring Boot 3**, siguiendo una **arquitectura limpia inspirada en hexagonal**.

---

## 💡 Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA + H2
- Spring Security + JWT
- Redis (para rate limiting)
- Lombok
- Maven

---

## 🏢 Arquitectura elegida

Se ha seguido una **arquitectura limpia inspirada en hexagonal** para mantener el código modular y testable, sin la complejidad del patrón completo `port/adapter`. En caso de necesitarse, adoptar una arquitectura hexagonal teórica sería sencillo.

```
com.salsa.test.salsa
├── domain
│   ├── model
│   └── service
├── infra
│   ├── persistence
│   ├── rest
│   ├── security
│   └── entity
├── config
└── SalsaTestApplication.java
```

---

## 🛠️ Componentes principales

### 🔐 Seguridad
- Autenticación JWT sin estado
- Usuarios en H2
- Rate limiting con Redis
- Consola H2 solo en desarrollo

### 💼 Ofertas de empleo
- CRUD + búsqueda por:
  - Título
  - Localización
  - Salario → `maxSalary >= salary`

### 📊 Ranking
- Reglas:
  - Ofertas recientes (< 7 días)
  - Mayor salario
  - Más vacantes abiertas
- Implementado con patrón **Strategy**, orden configurable desde `application.yml`

### 📄 Paginación
- Soporte nativo con `Pageable`

---

## ✅ Endpoints principales

### 🔐 Autenticación
```http
POST /auth/login
{
  "username": "empleador1",
  "password": "password"
}
```

### 💼 Ofertas
```http
GET /api/job-offers
Parámetros:
  - title, location, salary
  - page, size, sort
```

```http
GET /api/job-offers/{id}
```

```http
POST /api/job-offers
Content-Type: application/json
Body:
{
  "title": "Backend Dev",
  "companyId": "uuid",
  "companyName": "Salsa",
  "description": "Job description",
  "jobType": "FULL_TIME",
  "salaryMin": 1000,
  "salaryMax": 2000,
  "benefits": [],
  "extras": []
}
```

---

## 📁 Datos de ejemplo

Precargados desde `data.sql` (para MVP):

| usuario     | contraseña | rol       |
|-------------|------------|-----------|
| empleador1  | password   | EMPLOYER  |
| empleado1   | password   | EMPLOYEE  |

> En producción debería usarse algo como **Liquibase** o **Flyway**.

---

## 📦 Lanzamiento

### Requisitos
- Java 21
- Docker

### 1. Iniciar Redis
```bash
docker-compose up -d redis
```

### 2. Iniciar la aplicación
```bash
./mvnw spring-boot:run
```

### 3. Consola H2
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:jobsdb
User: sa
Password:
```

---

## 🧰 Tests

### Ejecutar tests
```bash
./mvnw test
```

- Tests unitarios y de integración
- Login, filtros, ranking, controladores

---

## ✏️ Extensiones futuras

- Filtro por tipo de contrato
- Filtro por beneficios
- Reglas configurables desde BBDD
- Workers en Redis o RabbitMQ

---

## 📋 Aclaraciones

- Arquitectura **limpia inspirada en hexagonal**, sin la complejidad del patrón completo para este MVP.
- El **ranking** usa patrón **Strategy**, permitiendo fácil extensión y configuración.
- El filtro de salario busca `maxSalary >= salary`. Ejemplo: `salary=1500` encuentra hasta 2000.
- Por tiempo, no se incluyó `@ControllerAdvice` para manejar errores globales.
- `data.sql` se usa solo como base de datos de ejemplo. En futuro se podría usar Liquibase/Flyway.
- Workers opcionales no implementados. Idealmente se cachearían rankings al crear ofertas.
- Diseñado para escalar: paginación, auth JWT sin estado, rate limit con Redis, estructura modular.

---

## 🎉 Autor

Desarrollado por Marcos Rojas como parte de la prueba técnica para Salsa.dev

