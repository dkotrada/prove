# Motivation
This project is designed as a showcase to demonstrate modern technologies, patterns, and best practices in software architecture, making it a practical reference for teams and individuals interested in modular Spring development.

Spring Modulith is used in the project to enable a clean, modular architecture that combines the simplicity of a monolith with strong boundaries and maintainability between business modules. It helps organize code by domains, improves testability, allows easier refactoring, and prepares the application for possible future migration to microservices—without introducing the complexity of a distributed system too early.

# Get up and running
Info: project uses java version 21
```sh
java -version
openjdk version "21.0.8" 2025-07-15 LTS
```
1. Clone repository
```sh
git clone https://github.com/dkotrada/prove.git
```

2. Setup provelib dependency in mavenLocal
```sh
 cd prove
./gradlew bootstrapProvelib
```

3. Build
```sh
./gradlew build
```

3. Start application
```sh
./gradlew bootRun
```

4. API Request
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