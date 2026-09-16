# Pelicanwork Platform

A file management and collaboration platform for teams.

## Tech Stack

- **Backend:** Spring Boot 4.1.1, Java 21, Spring Data JPA
- **Database:** PostgreSQL 15 (Docker)
- **Frontend:** Next.js, TypeScript, Tailwind CSS (coming soon)
- **DevOps:** Docker, Kubernetes, AWS (coming soon)

## Project Status

- [x] Day 1: Backend setup + Health endpoint
- [ ] Day 2: Database schema + JPA entities
- [ ] Day 3-4: Authentication (JWT)
- [ ] Day 5-6: Organization & Workspace CRUD
- [ ] Day 8-10: Frontend development
- [ ] Day 29: Deployment
- [ ] Day 30: Launch

## Quick Start

### Prerequisites
- Java 21
- Docker & Docker Compose
- Maven (or use Maven wrapper)

### Run Database
```bash
cd infra/docker
docker compose up -d
```

### Run Backend
```bash
cd backend/pelicanwork-backend
./mvnw spring-boot:run
```

### Test
```bash
curl http://localhost:8080/health
# Response: "OK"
```

## API Endpoints

- `GET /health` - Health check endpoint

## Development

This project is part of a 30-day intensive learning journey covering:
- DSA (daily practice)
- Java & Spring Boot
- System Design
- DevOps (Docker, Kubernetes)
- Frontend (Next.js)

## License

MIT
