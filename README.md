# Motivation
This project is designed as a showcase to demonstrate modern technologies, patterns, and best practices in software architecture, making it a practical reference for teams and individuals interested in modular Spring development.

Spring Modulith is used in the project to enable a clean, modular architecture that combines the simplicity of a monolith with strong boundaries and maintainability between business modules. It helps organize code by domains, improves testability, allows easier refactoring, and prepares the application for possible future migration to microservices—without introducing the complexity of a distributed system too early.

# Get up and running
Prerequisites: docker compose, git, sdk man
```sh
sdk current java
Using java version 21.0.6-amzn
```

Clone repository, change to projectfolder
```sh
git clone https://github.com/dkotrada/prove.git && cd prove
```

Setup provelib dependency in mavenLocal
```sh
./gradlew bootstrapProvelib
```

Start application
```sh
./gradlew bootRun
```

API Request
```sh
curl http://localhost:8080/orders/nonexistent
```

Example response
```json
{"timestamp":"2025-08-15T08:23:57.266461884","status":404,"error":"Not Found","message":"Order with ID nonexistent not found","errors":{}}
```
Dbmate
```shell
alias dbmate="docker compose --profile tools run --rm dbmate"
```
```shell
dbmate help
```
After dbmate usage
```shell
docker compose down
```

# Implementation Roadmap
- [x] Modules - [Spring Modulith](https://spring.io/projects/spring-modulith)
- [x] Events - [Application Events](https://docs.spring.io/spring-modulith/reference/events.html)
- [x] Migrations - [Dbmate](https://github.com/amacneil/dbmate?tab=readme-ov-file#dbmate)
- [x] Logging - Logback
- [x] Testcontainers - PostgresSQL
- [x] Integration Tests
- [ ] OpenAPI
- [ ] Spring Security
- [ ] JWT - Json Web Token
- [ ] RBAC - Role-Based Access Control
- [ ] Observability Actuator
- [ ] Metrics Micrometer
- [ ] ... 

