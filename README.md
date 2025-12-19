# ATS Resume Scanner

End-to-end sample app: Spring Boot 3 + PostgreSQL + React 18 (Vite) that scores resumes against JDs and seeded market datasets.

## Prerequisites
- Java 21, Maven wrapper
- Node 20+, npm
- Docker & docker-compose

## Quick start
```bash
# backend
cd backend
./mvnw spring-boot:run

# frontend
cd frontend
npm install
npm run dev
```

## Docker Compose
```bash
docker-compose up --build
```
Frontend at http://localhost:5173, backend at http://localhost:8080.

## Env vars
- DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
- JWT_SECRET
- RESUME_STORAGE_DIR (default ./data/resumes)
- VITE_API_URL (frontend)

## API docs
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## Tests
```bash
cd backend
./mvnw test
```

## Data
- Flyway migrations in backend/src/main/resources/db/migration
- Skill dictionary: backend/src/main/resources/data/skills.json
- Market datasets: backend/src/main/resources/data/market

## Future improvements
- PDF report export
- Grammar/salary/insight plugins via InsightProvider
- Live job board provider implementation
```