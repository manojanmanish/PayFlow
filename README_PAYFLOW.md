# PayFlow Application

A Spring Boot-based payment transaction system demonstrating modern Java best practices, layered architecture, and Spring Boot's production-ready features.

---

## Table of Contents

1. [Quick Start](#quick-start)
2. [Application Architecture](#application-architecture)
3. [Spring Boot Features in PayFlow](#spring-boot-features-in-payflow)
4. [API Endpoints](#api-endpoints)
5. [Database](#database)
6. [Project Structure](#project-structure)

---

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running the Application

#### Option 1: Using Maven (Recommended)
```bash
cd D:\JavaUpskilling\Projects\AssignmentPayFlow\PayFlowApp
mvn clean spring-boot:run
```

#### Option 2: Build and Run JAR
```bash
mvn clean package
java -jar target/payflow-*.jar
```

#### Option 3: Using mvnw (Maven Wrapper)
```bash
mvnw.cmd spring-boot:run
```

### Verify the Application Started
Once running, you should see:
```
2026-06-09 10:30:45.123 INFO ... Tomcat initialized...
2026-06-09 10:30:45.456 INFO ... Started PayflowApplication...
```

The application will be accessible at: **`http://localhost:8080`**

### H2 Console (In-Memory Database)
Access the database UI at: **`http://localhost:8080/h2-console`**
- **JDBC URL:** `jdbc:h2:mem:PayFlowDB`
- **User:** `admin`
- **Password:** `password`

---

## Application Architecture

PayFlow follows a **layered (n-tier) architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← HTTP Endpoints
│  (UserController, TransactionController)│
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer                   │  ← Business Logic
│  (UserService, TransactionService)      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer                   │  ← Data Access
│ (UserRepository, TransactionRepository) │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Entity & DTO Layer                 │  ← Data Models
│ (Users, Transactions, UserDto, etc.)    │
└─────────────────────────────────────────┘
```

### Layer Responsibilities

#### 1. **Controller Layer** (`Controller/`)
- **Entry point** for HTTP requests
- Validates incoming requests and routes to services
- Returns HTTP responses with appropriate status codes
- Handles REST endpoints

**Files:**
- `UserController.java` – Manages user creation and retrieval
- `TransactionController.java` – Processes payment transactions

**Example:** `POST /users` creates a new user by calling `UserService.addUser()`

#### 2. **Service Layer** (`Service/`)
- **Business logic** and core application rules
- Performs validation and data transformations
- Orchestrates database operations via repositories
- Handles transaction management (financial rules)

**Files:**
- `UserService.java` – User management logic (add, retrieve, search)
- `TransactionService.java` – Transaction processing (validates balance, inactive users, processes transfers)

**Example:** `TransactionService.addTrx()` validates users, checks balance, deducts/adds funds, and saves the transaction.

#### 3. **Repository Layer** (`Repository/`)
- **Data access abstraction** using Spring Data JPA
- Communicates with the database
- Provides CRUD operations and custom queries

**Files:**
- `UserRepository.java` – Queries for Users (findByUpiId, custom filters)
- `TransactionRepository.java` – Queries for Transactions

**Example:** `findByFilter(userId, upiId)` returns an `Optional<Users>` from the database.

#### 4. **Entity & DTO Layer** (`Entity/`, `DTOs/`)
- **Entity classes** – JPA-mapped database tables (Users, Transactions)
- **DTOs** – Data Transfer Objects for API responses (UserDto, TrxResponse)
- **Enums** – Type-safe constants (UserStatus: A/L/I, TrxStatus: Success/Failure)

**Files:**
- `Users.java` – Database entity with userId, userName, upiId, balance, phoneNumber, userStatus
- `Transactions.java` – Database entity for payment transactions
- `UserDto.java` – Simplified user response (userId, userName only)
- `TrxResponse.java` – Transaction response (trxId, message)
- `UserStatus.java` – Enum (A=Active, L=Locked, I=Inactive)
- `TrxStatus.java` – Enum (Success, Failure)

---

## Spring Boot Features in PayFlow

PayFlow leverages three core Spring Boot features that make it production-ready:

### 1. **Embedded Server**

#### What It Is
Spring Boot includes an embedded **Tomcat servlet container**. You don't need to deploy to an external application server—the WAR/JAR runs standalone.

#### How It Appears in PayFlow
- When you run `mvn spring-boot:run`, Spring Boot starts an **embedded Tomcat server on port 8080** automatically.
- No separate Apache/Tomcat installation needed.
- The application JAR (`target/payflow-*.jar`) is self-contained and executable.

#### Code Evidence
In `PayflowApplication.java`:
```java
@SpringBootApplication
public class PayflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayflowApplication.class, args);
    }
}
```
This single line starts the embedded server and the entire Spring context.

#### Benefit
- Simplified deployment (single JAR file)
- Consistent development and production environment
- No server configuration drift

---

### 2. **Auto-Configuration**

#### What It Is
Spring Boot automatically configures application components based on classpath dependencies and properties. You rarely need to write XML or Java bean configurations.

#### How It Appears in PayFlow

1. **JPA/Hibernate Auto-Configuration**
   - Detects `jakarta.persistence` on classpath → automatically configures EntityManager and JPA
   - Your repositories (`UserRepository`, `TransactionRepository`) extend `JpaRepository` and work instantly

2. **DataSource Auto-Configuration**
   - Detects H2 driver → creates an H2 DataSource automatically
   - Configured via `application.properties`:
   ```properties
   spring.datasource.url=jdbc:h2:mem:PayFlowDB
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=admin
   ```

3. **Spring Data REST Auto-Configuration**
   - Detects Spring Data repositories → auto-exposes JPA repositories as REST endpoints
   - `@Repository` and `JpaRepository` methods become available without writing controller code

4. **Jackson Auto-Configuration**
   - Detects Jackson on classpath → automatically configures JSON serialization
   - Your DTOs and entities serialize to JSON in responses without manual configuration

5. **Component Scanning Auto-Configuration**
   - `@SpringBootApplication` enables classpath scanning
   - `@Service`, `@Repository`, `@Controller` beans are auto-discovered and registered

#### No Manual Configuration Needed
You didn't write any XML files or Java `@Configuration` classes because Spring Boot did it for you. Compare this to traditional Spring:
- **Traditional Spring:** 300+ lines of XML `applicationContext.xml`
- **Spring Boot PayFlow:** Zero XML, one `application.properties`

---

### 3. **Production-Ready Defaults**

#### What It Is
Spring Boot applies sensible defaults for production environments: logging, monitoring, security, and performance settings.

#### How It Appears in PayFlow

1. **Logging Configuration**
   - Auto-configured with **SLF4J + Logback**
   - Logs to console with ISO timestamp, thread, log level, and message
   - No logging code needed; Spring Boot provides it

   Example output:
   ```
   2026-06-09T10:30:45.456+05:30 INFO  o.s.b.StartupInfoLogger : Started PayflowApplication in 2.153 seconds
   ```

2. **Error Handling & Graceful Responses**
   - Automatic exception handling via Spring Boot's error pages (or custom error handlers)
   - Invalid requests return proper HTTP status codes:
     - `400 Bad Request` for malformed JSON
     - `404 Not Found` when endpoints don't exist
     - `500 Internal Server Error` for unhandled exceptions

3. **Connection Pooling**
   - Auto-configures **HikariCP** connection pool for database connections
   - Prevents connection exhaustion in production
   - Default pool size: 10 connections (configurable)

4. **Server Configuration**
   - Embedded Tomcat defaults:
     - Max threads: 200
     - Graceful shutdown: 30 seconds
     - Connection timeout: 20 seconds
   - These prevent resource exhaustion under load

   Override in `application.properties` if needed:
   ```properties
   server.tomcat.threads.max=250
   server.shutdown=graceful
   server.shutdown.wait-time=60s
   ```

5. **SQL Initialization**
   - Spring Boot auto-initializes the H2 database on startup
   - Detects `schema.sql` and `data.sql` (if present) and executes them automatically

6. **Metrics & Monitoring**
   - Spring Boot Actuator (optional dependency) provides endpoints for health, metrics, and monitoring
   - Example: `GET /actuator/health` returns `{"status":"UP"}`

7. **Content Negotiation**
   - Automatic JSON serialization for REST responses
   - Content-Type headers automatically set to `application/json`

8. **Validation**
   - Spring validates `@RequestBody` automatically
   - Invalid data returns `400 Bad Request` with error details

#### Example: Error Handling in PayFlow
When you POST invalid JSON to `/users`:
```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d "invalid"
```

Spring Boot automatically returns:
```json
{
  "timestamp": "2026-06-09T10:30:45.123+05:30",
  "status": 400,
  "error": "Bad Request",
  "message": "JSON parse error: ...",
  "path": "/users"
}
```

This error handling is production-ready with no code needed.

---

## API Endpoints

### User Management

#### Create a User
```bash
POST /users
Content-Type: application/json

{
  "userName": "Alice",
  "upiId": "alice@upi",
  "balance": 5000.0,
  "phoneNumber": "9876543210",
  "userStatus": "A"
}
```

**Response (201 Created):**
```json
{
  "userId": 1,
  "userName": "Alice"
}
```

#### Get All Users
```bash
GET /users
```

#### Search User by ID or UPI
```bash
GET /users/search?userId=1
GET /users/search?upiId=alice@upi
```

---

### Transaction Management

#### Create a Transaction
```bash
POST /pay
Content-Type: application/json

{
  "fromUpiId": "alice@upi",
  "toUpiId": "bob@upi",
  "amount": 1000.0
}
```

**Response (201 Created):**
```json
{
  "trxId": 1,
  "message": "Transaction has been successfully processed."
}
```

---

## Database

### In-Memory H2 Database
PayFlow uses **H2**, an in-memory relational database—perfect for development and testing.

### Configuration
File: `src/main/resources/application.properties`
```properties
spring.application.name=payflow
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:PayFlowDB
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=password
```

### JPA Entity Mapping
PayFlow uses **JPA annotations** to auto-create tables:

**Users Table** (auto-created from `Users.java`):
| userId (PK) | userName | upiId (UNIQUE) | balance | phoneNumber | userStatus |
|---|---|---|---|---|---|
| 1 | Alice | alice@upi | 5000.0 | 9876543210 | A |

**Transactions Table** (auto-created from `Transactions.java`):
| trxId (PK) | fromUpiId | toUpiId | amount | trxStatus | message |
|---|---|---|---|---|---|
| 1 | alice@upi | bob@upi | 1000.0 | Success | Transfer completed |

---

## Project Structure

```
PayFlowApp/
├── src/main/java/com/assignment/payflow/
│   ├── PayflowApplication.java           ← Main Spring Boot entry point
│   ├── Controller/
│   │   ├── UserController.java           ← REST endpoints for users
│   │   └── TransactionController.java    ← REST endpoints for transactions
│   ├── Service/
│   │   ├── UserService.java              ← User business logic
│   │   └── TransactionService.java       ← Transaction business logic
│   ├── Repository/
│   │   ├── UserRepository.java           ← JPA queries for Users
│   │   └── TransactionRepository.java    ← JPA queries for Transactions
│   ├── Entity/
│   │   ├── Users.java                    ← JPA entity for users table
│   │   └── Transactions.java             ← JPA entity for transactions table
│   ├── DTOs/
│   │   ├── UserDto.java                  ← Response DTO for users
│   │   └── TrxResponse.java              ← Response DTO for transactions
│   └── Enums/
│       ├── UserStatus.java               ← User status enum (A/L/I)
│       └── TrxStatus.java                ← Transaction status enum
├── src/main/resources/
│   └── application.properties            ← Spring Boot configuration
├── src/test/
│   └── PayflowApplicationTests.java      ← Unit/integration tests
├── pom.xml                               ← Maven dependencies & build config
├── mvnw & mvnw.cmd                       ← Maven wrapper scripts
└── README.md                             ← This file
```

---

## Key Technologies

| Technology | Role | Notes |
|---|---|---|
| **Java 17+** | Programming Language | Modern features like records, sealed classes |
| **Spring Boot 3.x** | Framework | Embedded server, auto-config, production-ready |
| **Spring Data JPA** | ORM | Simplifies database operations |
| **Hibernate** | JPA Implementation | Auto-creates tables from entities |
| **H2 Database** | In-Memory DB | Development/testing database |
| **Maven** | Build Tool | Dependency management and build lifecycle |
| **Tomcat** | Servlet Container | Embedded in Spring Boot |
| **Jackson** | JSON Serialization | Converts objects to/from JSON |
| **Lombok** (optional) | Boilerplate Reduction | Generates getters/setters/constructors |

---

## Development Tips

### Running Tests
```bash
mvn test
```

### Building the JAR
```bash
mvn clean package
```

### Viewing Generated SQL
Add to `application.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Debugging
Run with debug flag:
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

Then attach a debugger to port 5005.

---

## Troubleshooting

| Issue | Solution |
|---|---|
| **Port 8080 already in use** | `spring.server.port=9090` in `application.properties` |
| **H2 console not accessible** | Verify `spring.h2.console.enabled=true` in properties |
| **NullPointerException in transaction** | Ensure both `fromUpiId` and `toUpiId` users exist |
| **Build fails with dependency errors** | Run `mvn clean install` to refresh Maven cache |

---

## Summary: Spring Boot's Magic in PayFlow

| Feature | PayFlow Benefit |
|---|---|
| **Embedded Server** | Deploy single JAR; no Tomcat installation needed |
| **Auto-Configuration** | Zero XML config; JPA, DataSource, JSON serialization work automatically |
| **Production-Ready Defaults** | Logging, error handling, connection pooling, graceful shutdown built-in |

This is why PayFlow runs with minimal configuration—Spring Boot handles the rest.

---

**Author:** PayFlow Development Team  
**Last Updated:** June 9, 2026  
**License:** MIT

