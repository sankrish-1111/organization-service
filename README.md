# Organization Group Service

Author: Santhanam

## Overview
This microservice manages groups and their hierarchy, including CRUD operations and user membership management, using Redis as the primary data store. It is designed for the "Organizations v1.2" initiative and follows all assessment requirements.

## Features
- Create, view (with inheritance), update, and delete groups
- Add, remove, and move users between groups
- Hierarchical data modeling and property inheritance
- Atomic operations for user movement
- RESTful API with OpenAPI/Swagger documentation
- Dockerized for easy deployment

## How to Run the Microservice

### Prerequisites
- Java 17 or higher
- Maven
- Docker (for containerized setup)

### Run Locally
1. Start Redis locally (default port 6379):
   ```sh
   docker run --name redis-local -p 6379:6379 -d redis:7.2-alpine
   ```
2. Build and start the Spring Boot microservice:
   ```sh
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```
3. The microservice API will be available at `http://localhost:8080`.

### Run with Docker Compose
1. Build the application:
   ```sh
   ./mvnw clean package
   ```
2. Start the microservice and Redis using Docker Compose:
   ```sh
   docker-compose up --build
   ```
3. The microservice API will be available at `http://localhost:8080`.

## API Endpoints
- `POST /api/v1/groups` - Create a group
- `GET /api/v1/groups/{uuid}` - View a group with inheritance
- `PUT /api/v1/groups/{uuid}` - Update a group
- `DELETE /api/v1/groups/{uuid}` - Delete a group (only if no children)
- `POST /api/v1/groups/{uuid}/users` - Add a user to a group
- `DELETE /api/v1/groups/{uuid}/users/{userId}` - Remove a user from a group
- `PUT /api/v1/users/{userId}/move` - Move a user to another group

## Swagger/OpenAPI
- API documentation is available at: `http://localhost:8080/swagger-ui.html`

## Testing
- Integration tests are provided in `src/test/java/com/santhanam/group/GroupInheritanceIntegrationTest.java`.
- To run tests:
   ```sh
   ./mvnw test
   ```

