# SalsaTest Backend - Prueba Técnica / Technical Test

Este proyecto es una implementación de la prueba técnica de backend para la empresa **Salsa.dev**, desarrollada en **Java 21** con **Spring Boot 3**, siguiendo una **arquitectura hexagonal** (Ports & Adapters).

This project is an implementation of the backend technical test for **Salsa.dev**, developed using **Java 21** and **Spring Boot 3**, following a **hexagonal architecture** (Ports & Adapters).

---

## 💡 Tecnologías utilizadas / Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA + H2 Database
- Spring Security + JWT
- Redis (para rate limiting / for rate limiting)
- Lombok
- MapStruct (DTO mapping)
- Maven

---

## 🏢 Arquitectura elegida / Chosen Architecture

Se ha seguido un enfoque **hexagonal (puertos y adaptadores)** para separar la lógica de dominio de la infraestructura.

A **hexagonal architecture (ports and adapters)** approach has been followed to separate domain logic from infrastructure.

```
com.salsa.test.salsa
├── domain
│   ├── model          -> Clases de dominio puro / Pure domain classes
│   ├── port           -> Interfaces para los adaptadores / Interfaces for adapters
│   └── service        -> Servicios de dominio / Domain services
├── infra
│   ├── persistence    -> Repositorios JPA / JPA repositories
│   ├── rest           -> Controladores REST / REST controllers
│   ├── security       -> JWT + filtros / JWT + filters
│   └── entity         -> Entidades JPA / JPA entities
├── config             -> Configuración / Configuration
└── SalsaTestApplication.java
```

---

## 🛠️ Componentes principales / Main Components

### 🔐 Seguridad / Security
- Autenticación basada en JWT / JWT-based authentication
- Usuarios desde H2 / Users from H2
- Middleware de Redis para limitar peticiones / Redis-based rate limiting
- Consola H2 sólo en entorno de desarrollo / H2 Console only in dev

### 💼 Ofertas de empleo / Job Offers
- CRUD básico + búsqueda por:
    - Título / Title
    - Localización / Location
    - Salario / Salary → `maxSalary >= salary`

### 📊 Ranking de resultados / Ranking
- Basado en reglas:
    - Ofertas recientes (< 7 días) / Recent offers
    - Mayor salario / Higher salary
    - Más ofertas abiertas / More open jobs
- Fácilmente ordenables y extendibles / Reorderable and extendable rules

### 📄 Paginación / Pagination
- `Pageable` nativo / Built-in pagination support
- `?page=0&size=5&sort=createdAt,desc`

---

## ✅ Endpoints principales / Main Endpoints

### 🔐 Autenticación / Authentication
```http
POST /auth/login
{
  "username": "empleador1",
  "password": "password"
}
```

### 💼 Ofertas / Job Offers
```http
GET /api/job-offers
Parámetros / Parameters:
  - title, location, salary
  - page, size, sort
```

---

## 📁 Datos de ejemplo / Sample Data

Precargados desde `data.sql` / Preloaded via `data.sql`:

| username    | password  | role      |
|-------------|-----------|-----------|
| empleador1  | password  | EMPLOYER  |
| empleado1   | password  | EMPLOYEE  |

---

## 📦 Lanzamiento / Running the Project

### Requisitos / Requirements
- Java 21
- Docker (para Redis / for Redis)

### 1. Ejecutar Redis / Start Redis
```bash
docker-compose up -d redis
```

### 2. Ejecutar Spring Boot / Start App
```bash
./mvnw spring-boot:run
```

### 3. Acceder a consola H2 / H2 Console
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:jobsdb
User: sa
Password:
```

---

## 🧪 Tests

- Tests unitarios e integración / Unit and integration tests
- Autenticación, filtros y ranking / Authentication, filtering, ranking

---

## ✏️ Extensiones futuras / Future Extensions

- Filtro por tipo de contrato / Job type filter
- Filtro por beneficios / Filter by benefits
- Orden de ranking desde BBDD / DB-configurable rule priority
- Workers en Redis o RabbitMQ / Redis or RabbitMQ workers for caching

---

## 🎉 Autor / Author

Desarrollado por Marcos Rojas como parte de la prueba técnica para Salsa.dev  
Developed by Marcos Rojas as part of the technical test for Salsa.dev

---

Para dudas o sugerencias, contacta conmigo o abre una incidencia.  
For questions or suggestions, open an issue or reach out.

¡Gracias! / Thank you 🚀