# Note App Server

A RESTful API server for a note-taking application built with Ktor framework. This server provides user authentication with JWT tokens and full CRUD operations for managing notes with PostgreSQL database integration.

## Project Overview

This project was created using the [Ktor Project Generator](https://start.ktor.io) and implements a secure note management system with user authentication.

### Tech Stack

- **Framework**: Ktor (Kotlin)
- **Database**: PostgreSQL with Exposed ORM
- **Authentication**: JWT (JSON Web Token)
- **Connection Pooling**: HikariCP
- **Serialization**: kotlinx.serialization & GSON

## Features

| Name                                                                   | Description                                                                        |
|------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Sessions](https://start.ktor.io/p/ktor-sessions)                      | Adds support for persistent sessions through cookies or headers                    |
| [GSON](https://start.ktor.io/p/ktor-gson)                              | Handles JSON serialization using GSON library                                      |
| [Authentication](https://start.ktor.io/p/auth)                         | Provides extension point for handling the Authorization header                     |
| [Call Logging](https://start.ktor.io/p/call-logging)                   | Logs client requests                                                               |
| [Authentication JWT](https://start.ktor.io/p/auth-jwt)                 | Handles JSON Web Token (JWT) bearer authentication scheme                          |
| [Exposed ORM](https://github.com/JetBrains/Exposed)                    | Kotlin SQL framework for database operations                                       |
| [HikariCP](https://github.com/brettwooldridge/HikariCP)                | High-performance JDBC connection pooling                                           |

## API Endpoints

### User Routes
- `POST /v1/users/register` - Register a new user
- `POST /v1/users/login` - Login and receive JWT token

### Note Routes (Authenticated)
- `POST /v1/notes/create` - Create a new note
- `GET /v1/notes` - Get all notes for the authenticated user
- `PUT /v1/notes/update` - Update an existing note
- `DELETE /v1/notes/delete` - Delete a note

## Environment Variables

The application requires the following environment variables to be set before running:

| Variable          | Description                                          | Example Value                                                                  |
|-------------------|------------------------------------------------------|--------------------------------------------------------------------------------|
| `DATABASE_URL`    | PostgreSQL JDBC connection URL with credentials      | `jdbc:postgresql:your_database_name?user=your_username&password=your_password` |
| `JDBC_DRIVER`     | JDBC driver class name for database connection       | `org.postgresql.Driver`                                                        |
| `JWT_SECRET`      | Secret key for JWT token generation and verification | `your_jwt_secret`                                                              |
| `HASH_SECRET_KEY` | Secret key for password hashing (HmacSHA1)           | `your_hash_secret_key`                                                         |

### Setting Environment Variables

**Windows (CMD):**
```cmd
set DATABASE_URL=jdbc:postgresql:your_database_name?user=your_username^&password=your_password
set HASH_SECRET_KEY=your_hash_secret_key
set JDBC_DRIVER=org.postgresql.Driver
set JWT_SECRET=your_jwt_secret
```

**Windows (PowerShell):**
```powershell
$env:DATABASE_URL="jdbc:postgresql:your_database_name?user=your_username&password=your_password"
$env:HASH_SECRET_KEY="your_hash_secret_key"
$env:JDBC_DRIVER="org.postgresql.Driver"
$env:JWT_SECRET="your_jwt_secret"
```

**Linux/macOS:**
```bash
export DATABASE_URL="jdbc:postgresql:your_database_name?user=your_username&password=your_password"
export HASH_SECRET_KEY="your_hash_secret_key"
export JDBC_DRIVER="org.postgresql.Driver"
export JWT_SECRET="your_jwt_secret"
```

**All in one line (for convenience):**
```
DATABASE_URL=jdbc:postgresql:your_database_name?user=your_username&password=your_password;HASH_SECRET_KEY=your_hash_secret_key;JDBC_DRIVER=org.postgresql.Driver;JWT_SECRET=your_jwt_secret
```

## Database Setup

1. Install PostgreSQL on your system
2. Create a database with your preferred name:
   ```sql
   CREATE DATABASE your_database_name;
   ```
3. The application will automatically create the required tables (`UserTable` and `NoteTable`) on startup

## Building & Running

### Prerequisites
- JDK 11 or higher
- PostgreSQL database
- Gradle (included via wrapper)

### Run the Application

1. Set the required environment variables (see above)
2. Run the server using Gradle:

**Windows:**
```cmd
gradlew.bat run
```

**Linux/macOS:**
```bash
./gradlew run
```

### Other Gradle Tasks

| Task                                  | Description                                                          |
|---------------------------------------|----------------------------------------------------------------------|
| `gradlew test`                        | Run the tests                                                        |
| `gradlew build`                       | Build everything                                                     |
| `gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `gradlew buildImage`                  | Build the docker image to use with the fat JAR                       |
| `gradlew publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `gradlew run`                         | Run the server                                                       |
| `gradlew runDocker`                   | Run using the local docker image                                     |

### Successful Startup

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Project Structure

```
src/main/kotlin/com/codeturtle/
├── application/
│   ├── Application.kt      # Main application entry point
│   ├── Monitoring.kt       # Logging configuration
│   ├── Routing.kt          # Route configuration
│   ├── Security.kt         # JWT authentication setup
│   └── Serialization.kt    # JSON serialization setup
├── authentication/
│   ├── Authenticate.kt     # Password hashing utility
│   └── JWTService.kt       # JWT token generation and validation
├── data/
│   ├── model/              # Data models
│   │   ├── auth/           # User, LoginRequest, RegisterRequest, SimpleResponse
│   │   └── note/           # Note model
│   └── table/              # Database table definitions
├── repository/
│   ├── DatabaseFactory.kt  # Database connection and initialization
│   ├── NoteRepo.kt         # Note data access layer
│   └── UserRepo.kt         # User data access layer
└── routes/
    ├── NoteRoutes.kt       # Note-related endpoints
    └── UserRoutes.kt       # User-related endpoints
```

## Useful Links

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- [Exposed Wiki](https://github.com/JetBrains/Exposed/wiki)
- [JWT.io](https://jwt.io/) - Learn more about JSON Web Tokens

