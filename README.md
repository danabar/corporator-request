# Corporate Requests Management API

## Project Overview
This project is a Spring Boot application designed to manage corporate requests. It provides RESTful APIs to handle user-related operations, including creating, retrieving, updating, and deleting users. The project also includes entities for managing attachments and request statuses, with a focus on data validation and persistence using JPA.

## Technologies Used
- **Java:** 1.8
- **Spring Boot:** 2.5.4
- **Spring Data JPA:** For database operations
- **H2 Database:** In-memory database for development
- **Lombok:** For reducing boilerplate code
- **JUnit 5:** For unit testing
- **Mockito:** For mocking in unit tests
- **Maven:** For dependency management and build automation

## Project Structure
```
com.example.corporaterequests
├── api.controller      # Contains the REST controllers (e.g., UserController)
├── dto.entity          # Contains the JPA entities (e.g., User, Attachment, RequestStatus)
├── dto.repository      # Contains the JPA repositories (e.g., AttachmentRepository)
├── service             # Contains the service layer (e.g., UserService)
├── dto.request         # Contains the DTOs for request payloads (e.g., UserRequest)
└── dto.response        # Contains the DTOs for response payloads (e.g., UserResponse)
```

## How to Run the Project

### Prerequisites
- Java 8 installed
- Maven installed

### Clone the Repository
```bash
git clone <repository-url>
cd corporate-requests
```
### Configure Database:

Update the application.properties file with your database credentials:
properties
Copy
spring.datasource.url=jdbc:postgresql://localhost:5432/corporate_requests
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

### Access the API
The application will start on `http://localhost:8080`.

Use tools like Postman or cURL to interact with the API endpoints.

## API Endpoints
| Method | Endpoint         | Description            |
|--------|-----------------|------------------------|
| POST   | `/users`        | Create User           |
| GET    | `/users/{id}`   | Get User by ID        |
| PUT    | `/users/{id}`   | Update User           |
| DELETE | `/users/{id}`   | Delete User           |

## Database
The project uses an H2 in-memory database for development purposes.

You can access the H2 console at `http://localhost:8080/h2-console` (if enabled in `application.properties`).

## Testing
Unit tests are written using JUnit 5 and Mockito.

Run tests using:
```bash
mvn test
```
##Docker Deployment
###Build Docker Image:

```bash
docker build -t corporate-requests-app .
```
###Run Docker Container:
```bash
docker run -p 8080:8080 corporate-requests-app
```

##Database Schema Design
The database schema is designed to support the entities: User, and Attachment. Below is the schema:

a. User Table
sql

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    civil_id VARCHAR(255) UNIQUE NOT NULL,
    expiry_date DATE NOT NULL
);
b. Attachment Table
sql

CREATE TABLE attachments (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id)
);
##Entity Relationships

User and Attachment: One-to-Many (One user can have multiple attachments).



