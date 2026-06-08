# PayFlow

Payment application for transactions between users built with Spring Boot, Spring Data JPA, and H2.

## How to run the app

### Prerequisites
- Java 17+
- Maven 3.6+

### Run with Maven
```bash
cd D:\JavaUpskilling\Projects\AssignmentPayFlow\PayFlowApp
mvn clean spring-boot:run
```

### Build and run the JAR
```bash
mvn clean package
java -jar target/payflow-*.jar
```

### H2 console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:PayFlowDB`
- Username: `admin`
- Password: `password`

## Layered architecture

### Controller layer
Handles HTTP requests and responses.
- `UserController` exposes `/users`
- `TransactionController` exposes `/pay`

### Service layer
Contains business logic.
- `UserService` creates and fetches users
- `TransactionService` validates users, checks balance, and processes transfers

### Repository layer
Provides data access using Spring Data JPA.
- `UserRepository`
- `TransactionRepository`

### Entity / DTO layer
- Entities map to database tables: `Users`, `Transactions`
- DTOs shape API responses: `UserDto`, `TrxResponse`
- Enums keep status values type-safe: `UserStatus`, `TrxStatus`

## Spring Boot features in PayFlow

### 1. Embedded server
Spring Boot starts embedded Tomcat automatically, so the app runs as a standalone JAR without installing an external application server.

### 2. Auto-configuration
Spring Boot auto-configures JPA, the H2 datasource, JSON serialization, and component scanning based on the dependencies and annotations in the project.

### 3. Production-ready defaults
The app gets sensible defaults for logging, error handling, connection pooling, and HTTP handling with minimal setup.

## Custom query approaches in Spring Data JPA

PayFlow uses repository methods for database access. There are three common ways to write custom queries:

### 1. Derived method names
Spring Data builds the query from the method name.

Example:
```java
Optional<Users> findByUpiId(String upiId);
List<Users> findByBalanceGreaterThan(double amount);
```

**Pros:**
- Very readable
- No query string to maintain
- Best for simple filters and CRUD-style lookups

**Cons:**
- Method names can become long and hard to read for complex filters
- Not ideal for advanced joins or grouped logic

### 2. `@Query` with JPQL
You write a query using entity names and entity fields, not table names.

Example:
```java
@Query("SELECT u FROM Users u WHERE u.balance > :amount")
List<Users> findUsersWithBalanceGreaterThan(double amount);
```

**Pros:**
- Still database-agnostic because it works with entities
- Clear for custom filtering and joins
- Easier to maintain than long derived method names

**Cons:**
- You must know JPQL syntax
- Slightly more verbose than derived methods

### 3. Native SQL
You write raw SQL directly against database tables.

Example:
```java
@Query(value = "SELECT * FROM users WHERE balance > ?1", nativeQuery = true)
List<Users> findUsersWithBalanceGreaterThanNative(double amount);
```

**Pros:**
- Full access to database-specific SQL features
- Useful for complex performance tuning or vendor-specific functions

**Cons:**
- Tied to a specific database schema and SQL dialect
- Harder to refactor when table or column names change
- Less portable across databases
- Easier to introduce bugs because it bypasses JPA entity mapping rules

### Why native queries are the least preferred
Native SQL is usually the last choice because it reduces portability and maintainability. In a Spring Boot project like PayFlow, JPQL and derived methods are preferred first because:

- they work with entity classes instead of table names,
- they are easier to refactor,
- they keep the code closer to the domain model,
- and they let Spring Data JPA manage more of the query generation for you.

Use native queries only when JPQL cannot express the query well enough or when you need a database-specific feature.

## Project structure

```text
src/main/java/com/assignment/payflow/
├── Controller/
├── DTOs/
├── Entity/
├── Enums/
├── Repository/
└── Service/
```

## Example endpoints

### Create a user
```bash
curl -X POST http://localhost:8080/users -H "Content-Type: application/json" -d "{\"userName\":\"Alice\",\"upiId\":\"alice@upi\",\"balance\":5000.0,\"phoneNumber\":\"9876543210\"}"
```

### Create a transaction
```bash
curl -X POST http://localhost:8080/pay -H "Content-Type: application/json" -d "{\"fromUpiId\":\"alice@upi\",\"toUpiId\":\"bob@upi\",\"amount\":1000.0}"
```
