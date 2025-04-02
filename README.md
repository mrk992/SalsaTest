# SalsaTest Backend - Technical Test

This project is an implementation of the backend technical test for **Salsa.dev**, developed using **Java 21** and **Spring Boot 3**, following a **hexagonal architecture** (Ports & Adapters).

---

## 💡 Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA + H2 Database
- Spring Security + JWT
- Redis (for rate limiting)
- Lombok
- MapStruct (DTO mapping)
- Maven

---

## 🏢 Chosen Architecture

A **hexagonal architecture (ports and adapters)** approach has been followed to separate domain logic from infrastructure.

```
com.salsa.test.salsa
├── domain
│   ├── model          -> Pure domain classes
│   ├── port           -> Interfaces for adapters
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
- Reorderable and extendable rules via config

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

---

## 📁 Sample Data

Preloaded via `data.sql`:

| username    | password  | role      |
|-------------|-----------|-----------|
| empleador1  | password  | EMPLOYER  |
| empleado1   | password  | EMPLOYEE  |

---

## 📦 Running the Project

### Requirements
- Java 21
- Docker (for Redis)

### 1. Start Redis
```bash
docker-compose up -d redis
```

### 2. Start Spring Boot
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

## 🧪 Tests

- Unit and integration tests
- Authentication, filtering, ranking

---

## ✏️ Future Extensions

- Job type filter
- Benefits filter
- DB-configurable rule priority
- Redis or RabbitMQ workers for caching

---

## 🧾 Design Clarifications

This project follows a lightweight **hexagonal architecture** for separation of concerns, though the folder structure has been simplified by omitting detailed `port/adapter` layers, in order to keep the implementation lean for a technical test and MVP scope.

The **ranking logic** uses the **Strategy pattern**, allowing new ranking rules to be added easily without modifying existing code. Rule priority is configurable via application properties.

Salary filtering supports both "greater than or equal to" and range queries (e.g., searching with `salary=1000` will match offers with `maxSalary >= 1000`).

Error handling is basic (bean validation + 400) and does not include a global exception handler due to time constraints. A typical error handler in Spring Boot uses `@ControllerAdvice` + `@ExceptionHandler`.

The optional background workers were not implemented, but the intended approach would be to queue ranking calculations when offers are created, and store ranked results in a cache like Redis to speed up searches.

**Scalability**: The system supports pagination, filtering, stateless JWT auth (for horizontal scaling), Redis-based rate limiting, and a modular structure that allows easy integration with scalable tools like Elasticsearch, Kafka, or distributed caches.

---

## 🎉 Author

Developed by Marcos Rojas as part of the technical test for Salsa.dev

For questions or suggestions, open an issue or reach out.  
Thank you 🚀

---

# SalsaTest Backend - Prueba Técnica

Este proyecto es una implementación de la prueba técnica de backend para la empresa **Salsa.dev**, desarrollada en **Java 21** con **Spring Boot 3**, siguiendo una **arquitectura hexagonal** (Ports & Adapters).

---

## 💡 Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA + H2
- Spring Security + JWT
- Redis (para rate limiting)
- Lombok
- MapStruct
- Maven

---

## 🏢 Arquitectura elegida

Se ha seguido un enfoque **hexagonal** para separar la lógica de dominio de la infraestructura.

```
com.salsa.test.salsa
├── domain
│   ├── model
│   ├── port
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
- Autenticación JWT
- Usuarios en H2
- Rate limiting con Redis
- Consola H2 solo en dev

### 💼 Ofertas
- CRUD básico + filtros por:
    - Título
    - Localización
    - Salario → `maxSalary >= salary`

### 📊 Ranking
- Reglas:
    - Ofertas recientes (< 7 días)
    - Mayor salario
    - Más ofertas abiertas
- Reglas ordenables y extendibles vía config

### 📄 Paginación
- Soporte `Pageable` nativo

---

## ✅ Endpoints

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

---

## 📁 Datos de ejemplo

Precargados vía `data.sql`:

| usuario     | contraseña | rol       |
|-------------|------------|-----------|
| empleador1  | password   | EMPLOYER  |
| empleado1   | password   | EMPLOYEE  |

---

## 📦 Lanzamiento

### Requisitos
- Java 21
- Docker

### 1. Iniciar Redis
```bash
docker-compose up -d redis
```

### 2. Iniciar la app
```bash
./mvnw spring-boot:run
```

### 3. Acceder a H2
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:jobsdb
User: sa
Password:
```

---

## 🧪 Tests

- Unitarios e integración
- Login, filtros, ranking

---

## ✏️ Extensiones futuras

- Filtro por tipo de contrato
- Filtro por beneficios
- Orden de reglas desde BBDD
- Workers en Redis o RabbitMQ

---

## 🧾 Aclaraciones

He usado una arquitectura **hexagonal** pero con estructura simplificada (sin `port/adapter` explícitos) para enfocarme en la lógica del test y no sobrecargar el diseño.

El sistema de **ranking** sigue el patrón **Strategy**, para facilitar la adición de nuevas reglas. El orden se configura desde `application.yml`.

En los filtros de búsqueda, el salario se considera válido si `maxSalary >= salary` (ej. búsqueda con `salary=1000` encuentra ofertas con hasta 1500).

No he implementado un `@ControllerAdvice` por tiempo, pero sería ideal para centralizar errores en producción.

Para los workers opcionales, planteo usar colas que procesen rankings tras la creación de una oferta y cachear los resultados.

**Escalabilidad**: Se usa paginación, JWT sin estado (escalado horizontal), rate limiting con Redis y una estructura extensible que permite fácilmente integrar herramientas como Elasticsearch, colas de mensajes o cachés distribuidas.

---

## 🎉 Autor

Desarrollado por Marcos Rojas como parte de la prueba técnica para Salsa.dev

Para dudas o sugerencias, contáctame o abre una incidencia.  
¡Gracias! 🚀