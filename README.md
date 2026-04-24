# 🗳️ VoteSystem — Spring Boot + JDBC Voting App

Java Experiential Learning project migrated from console to full-stack web app.

## Tech Stack
| Layer      | Technology                        |
|------------|-----------------------------------|
| Backend    | Java 17 + Spring Boot 3.2         |
| Database   | H2 (dev) / MySQL (prod)           |
| DB Access  | Spring JDBC (`JdbcTemplate`)      |
| Frontend   | HTML5 + CSS3 + Vanilla JS         |
| Build      | Maven                             |

## Project Structure
```
voting-app/
├── src/main/java/com/voting/
│   ├── VotingApplication.java        ← Spring Boot entry point
│   ├── model/
│   │   ├── Person.java               ← Base class (from your console project)
│   │   ├── Voter.java                ← Extends Person
│   │   ├── Candidate.java            ← Contains getVotePercentage()
│   │   └── VoteRequest.java          ← REST request DTO
│   ├── repository/
│   │   ├── CandidateRepository.java  ← JDBC data access (candidates)
│   │   └── VoterRepository.java      ← JDBC data access (voters)
│   ├── service/
│   │   └── VotingService.java        ← Business logic
│   └── controller/
│       └── VotingController.java     ← REST API endpoints
├── src/main/resources/
│   ├── application.properties        ← DB config (H2 default, MySQL commented)
│   ├── schema.sql                    ← Table definitions
│   ├── data.sql                      ← Sample seed data
│   └── static/
│       ├── index.html                ← Frontend
│       ├── css/style.css
│       └── js/app.js
└── pom.xml
```

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps
```bash
# 1. Navigate to project
cd voting-app

# 2. Build and run
mvn spring-boot:run

# 3. Open browser
open http://localhost:8080
```

### H2 Database Console (dev only)
Visit: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:votingdb`
- Username: `sa`
- Password: *(leave blank)*

## REST API Endpoints

| Method | Endpoint           | Description            |
|--------|--------------------|------------------------|
| GET    | /api/candidates    | List all candidates    |
| POST   | /api/candidates    | Add a candidate        |
| DELETE | /api/candidates/id | Remove a candidate     |
| GET    | /api/voters        | List all voters        |
| POST   | /api/voters        | Register a voter       |
| DELETE | /api/voters/id     | Remove a voter         |
| POST   | /api/vote          | Cast a vote            |
| GET    | /api/results       | Get election results   |
| GET    | /api/dashboard     | Admin stats            |

## Switch to MySQL (Production)

1. In `application.properties`, comment out the H2 block and uncomment MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/votingdb
spring.datasource.username=root
spring.datasource.password=yourpassword
```

2. In `pom.xml`, uncomment the MySQL dependency and comment out H2.

3. Create the MySQL database:
```sql
CREATE DATABASE votingdb;
```

4. Re-run the app — `schema.sql` will auto-create the tables.
