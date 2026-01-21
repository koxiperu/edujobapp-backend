# EduJob Application Tracker - Backend

## Project Overview
This is the backend repository for the **EduJob Application Tracker**, a full-stack application developed as the Final Project for the **CNFPC Full Stack Developer Training 2026**.

The application allows users to track their job and study applications, manage documents, and view dashboard statistics. It includes a role-based system with features for Applicants (Users) and Administrators, demonstrating proficiency in backend development, API design, and database management.

## Technical Stack
- **Framework:** Spring Boot 4.0.1
- **Language:** Java 21
- **Database:** PostgreSQL
- **Security:** Spring Security + JWT (Stateless)
- **Documentation:** OpenAPI / Swagger UI
- **Build Tool:** Maven

## Prerequisites
Before running the application, ensure you have the following installed:
- **Java 21** (JDK)
- **PostgreSQL** (Service must be running)
- **Git**

## Installation & Setup

### 1. Database Setup
The application requires a PostgreSQL database named `edujobapp`. You can create this using your preferred tool (pgAdmin, CLI, etc.).

**Using Command Line:**
```bash
createdb edujobapp
```

**Using psql console:**
```sql
CREATE DATABASE edujobapp;
```

### 2. Configuration
The application comes pre-configured with default settings for a local development environment.
- **Default DB Username:** `postgres`
- **Default DB Password:** `password`
- **Server Port:** `8080`

If your local PostgreSQL credentials differ, you can override them in `src/main/resources/application.properties` or by setting the following environment variables:
- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

### 3. Running the Application
This project includes the Maven Wrapper, so you do not need to install Maven globally.

1. **Clone the repository** (if you haven't already):
   ```bash
   git clone <repository-url>
   cd edujobapp-backend
   ```

2. **Run the application:**
   - **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   - **Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

The application will start and listen on `http://localhost:8080`.

## Database Seeding & Test Credentials
On the first run, if the database is empty, the application will automatically seed it with initial data, including roles, users, companies, and sample applications.

Use the following credentials to test the application:

| Role | Username | Password | Permissions |
|------|----------|----------|-------------|
| **Admin** | `admin` | `admin` | Manage Users (CRUD), View all users |
| **User** | `test` | `test` | Manage own Applications, Documents, Companies, Dashboard |

## API Documentation
The backend provides full interactive documentation via Swagger UI. Once the application is running, access it here:

- **Swagger UI:** [http://localhost:8080/docs](http://localhost:8080/docs)
- **OpenAPI Spec:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Frontend Application
**Note:** This repository contains only the **backend API** logic.

The user interface is built with **ReactJS**. For instructions on how to install, configure, and run the frontend application, please refer to the `README.md` file located in the frontend project directory.

## Evaluation & Submission
- **Project:** Final Project (Module: Full Stack Developer)
- **Deadline:** Thursday, 22/01/2026
- **Deliverables:** Source code, README, Swagger docs, Database schema.

---
*Created for CNFPC Full Stack Developer Training 2026*
