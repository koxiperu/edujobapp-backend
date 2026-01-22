# 1. Project Requirements

## 1.1. Overview
Build full-stack application EduJob Application Tracker.

## 1.2. Technical Stack
- **Frontend:** ReactJS
- **Backend:** Spring Boot (Java)
- **Database:** PostgreSQL

## 1.3. Core Requirements

### Backend (Spring Boot):
- Build RESTful API endpoints for user authentification and authorisation, user's applications/documenta/companies management, users management for ADMIN. (Frontend Fully Integrated)
#### API structure
**/api/auth** 
   * /register: POST - Register a new user. 
   * /login: POST - Authenticate and receive a token (JWT).

**/api/public - for unauthenticated users**
   * /jobs
        GET - Get job advertisements from an external third-party API.

**/api/users**
   * /me
        GET - Get the current user's profile.
        PUT -  Update the current user's profile.
   * /
        GET - (ADMIN) Get a list of all users.
   * /{id}
        GET - (ADMIN) Get a specific user by ID.
        PUT - (ADMIN) Update a user's information.
        DELETE - (ADMIN) Delete a user.

**/api/applications - for the logged-in user**
   * /
        GET - Get all applications .
        POST - Create a new application.
   * /{id}
        GET - Get a single application by ID.
        PUT - Update an application.
        DELETE - Delete an application.

**/api/companies - for the logged-in user**
   * /
        GET - Get all companies for the logged-in user.
        POST - Create a new company.
   * /{id}
        GET - Get a single company by ID.
        PUT - Update a company.
        DELETE - Delete a company.
   * /{id}/applications
        GET - Get all applications for a specific company.

**/api/documents - for the logged-in user**
   * /
        GET - Get all documents for the logged-in user.
   * /upload
        POST - Upload a new document.
   * /{id}
        GET - Get metadata for a single document.
        PUT - Update a document's metadata.
        DELETE - Delete a document.
   * /{id}/download
        GET -  Download a document.
   * /{id}/applications
        GET - Get all applications using a specific document.

**/api/dashboard - of logged-in user**
   GET - Get dashboard statistics and data for the logged-in user.
   Response includes:
   1.  Total count of documents and list of document file names.
   2.  Total count of companies and list of company names.
   3.  Total count of applications and list of all applications (full details) for frontend filtering.
   4.  Applications breakdown by status and type.
   Frontend will use this data to display critical applications (deadline < 1 week).

### Database (PostgreSQL):
#### Role Entity (roles)
Defines user permissions for authorization. ADMIN and APPLICANT for now, SUPERVISOR for the future.
- id (PK)	(Unique role ID)
- name	(Role name (String: ADMIN, USER))
ADMIN role is designed for managing other users (only update, delete, view user's info). ADMIN cannot self-register or manage their own profile data beyond basic updates allowed for all users; their primary function is system administration. ADMIN does not have access to personal user data like documents, applications or user's list of companies.

#### User Entity (users)
Represents any system user. 
- id (PK)	(Unique, NotNullreferenced in applications)
- username (Uniqie, NotNull, Login username)
- password (NotNull, Hashed password for Spring Security)
- role_id (FK → roles, NotNull, Role of the user)
- email	(NotNull, Unique, Contact email)
- first_name	(NotNull, First name)
- last_name	(NotNull, Last name)
- birth_date	(Date of birth)
- phone	(Phone number)


#### Company Entity (companies)
Represents a university, lycée, course provider, or employer.
- id (PK)	(NotNull, Unique; referenced by applications)
- name	(NotNull, Company name)
- type	(NotNull, UNIVERSITY / EMPLOYER / LYCEE / COURSE)
- country	(NotNull, Country)
- address	(Address)
- website	(Website URL)
- phone	(Contact phone)
- email	(Contact email)
- user_id (NotNull, FK → users,	Owner of the company)

#### Document Entity (documents)
Represents uploaded files (PDFs, images, Excel, etc.) stored as binary content inside DB (max 5MB each file and not more than 1000, otherwise performance issues). But for production external storage is better.
One document may be reused across multiple applications.
- id (PK)	(NotNull, Unique, referenced by applications)
- file_name	(NotNull, File name that user want to be displayed)
- content_type	(pdf, picture, document, excel, etc. or brief description)
- upload_date	(File upload timestamp, defined at the moment of saving in db as current timestamp)
- data	(Binary content - byte array)
- doc_status	(NotNull, ENUM: READY / IN PROGRESS / NEED TO UPDATE)
- user_id (NotNull, FK → users,	Owner of the documents)

#### Application Entity (applications)
Represents a job or study application submitted by a user.
- id (PK)	(NotNull, Unique; referenced in app_doc)
- title	(NotNull, Application title, e.g., "Software Engineer Intern")
- user_id (NotNull, FK → users,	Owner of the application)
- company_id (NotNull, FK → companies,	Companies being applied to)
- application_type	(NotNull, ENUM: JOB / UNIVERSITY / LYCEE / COURSE)
- creation_date	(Record creation date, defined by assigning the current timestamp when saving in db)
- submit_date	(Date of submission)
- submit_deadline	(Application deadline)
- response_deadline	(Expected response date)
- app_status	(NotNull, ENUM: DRAFT / SUBMITTED / UNDER_REVIEW / ACCEPTED / REJECTED)
- description	(Detailed description of the job offer or course program)
- response_notes	(Optional notes about the result or feedback)

#### Join table (Many-to-Many): Application-Document (app-doc)
Implements Many-to-Many between applications and documents.
- application_id (FK → applications, Application ID)
- document_id (FK → documents,	Document ID)
Primary Key: (application_id, document_id)


### Authentication & Authorization:
- Implement user authentication.
- Secure password handling (passwords must be hashed in the database).
- Implement Role-based access control (e.g., USER vs. ADMIN).

#### Frontend (ReactJS)
- Build a responsive web app.
- Implement proper state management.
- Include form validation.
- Follow UX design best practices.

# 2. Architecture & Implementation Details (For Developer)
## 2.1. Project Initialization & Dependencies
The project is built with **Spring Boot 4.0.1** and **Java 21**. Essential dependencies used:
- `spring-boot-starter-webmvc`: Core RESTful API support.
- `spring-boot-starter-data-jpa`: Persistent storage with Hibernate.
- `spring-boot-starter-validation`: Bean validation for DTOs.
- `spring-boot-starter-security`: RBAC and secure endpoint configuration.
- `postgresql`: Database driver.
- `jjwt` (api, impl, jackson 0.12.3): Stateless JWT authentication.
- `springdoc-openapi-starter-webmvc-ui` (2.7.0): Swagger UI documentation.
- `spring-boot-starter-test`: Unit and integration testing.

Folder structure follows standard Spring Boot conventions:
```
/src
├───main
│   ├───java
│   │   └───lu
│   │       └───cnfpc
│   │           └───edujobapp
│   │               ├───config         # App configs, DatabaseSeeder, OpenAPI
│   │               ├───controller     # API Endpoints
│   │               ├───dto            # Request/Response/Error DTOs
│   │               ├───entity         # JPA Entities and Enums
│   │               ├───exception      # Custom exceptions and GlobalExceptionHandler
│   │               ├───mapper         # Entity-DTO mapping logic
│   │               ├───repository     # Spring Data JPA interfaces
│   │               ├───security       # JWT filters, UserPrincipal, SecurityConfig
│   │               └───service        # Business logic and Auth service
│   └───resources                      # application.properties
└───test                               # JUnit 5 and Mockito tests
```

## 2.2. Configuration (application.properties)
Configured for PostgreSQL. Supports environment variable overrides:
- `DATABASE_URL`: `jdbc:postgresql://localhost:5432/edujobapp`
- `DATABASE_USERNAME`: `postgres`
- `DATABASE_PASSWORD`: `password`
- `JWT_SECRET`: HS256 secret key.
- `JWT_EXPIRATION`: 86400000 ms (24 hours).

## 2.3. Domain Entities & Enums
Specific Enums used to ensure data integrity:
- `EApplicationStatus`: `DRAFT`, `SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`.
- `EApplicationType`: `JOB`, `UNIVERSITY`, `LYCEE`, `COURSE`.
- `ECompanyType`: `UNIVERSITY`, `EMPLOYER`, `LYCEE`, `COURSE`.
- `EDocumentStatus`: `READY`, `IN_PROGRESS`, `NEED_TO_UPDATE`.
- `ERole`: `ROLE_USER`, `ROLE_ADMIN`.

## 2.4. Database Seeding & Testing
The `DatabaseSeeder` runs on startup, initializing:
- **Roles:** `USER`, `ADMIN`.
- **Users:**
    1. **Admin:** `admin` / `admin` (Role: `ADMIN`, manages users).
    2. **Test User:** `test` / `test` (Role: `USER`, owns documents/applications).
- **Companies:** 10 pre-defined Luxembourg companies (POST, Amazon, ArcelorMittal, etc.).
- **Documents:** Sample files loaded from `config/seed_documents/` (CVs, letters).
- **Applications:** 8 varied applications for the 'test' user to populate the dashboard.

## 2.5. Security & Authentication
Implemented via a stateless JWT filter chain:
- `JwtAuthenticationFilter`: Intercepts every request, validates the token, and sets the `SecurityContext`.
- `UserPrincipal`: Implements `UserDetails` to store authenticated user info.
- `RestAuthenticationEntryPoint`: Returns 401 Unauthorized for failed auth.
- `RestAccessDeniedHandler`: Returns 403 Forbidden for insufficient permissions.

## 2.6. Global Exception Handling
A `@ControllerAdvice` maps custom and standard exceptions to a structured `ErrorResponse`:
- `UsernameAlreadyExistsException` / `EmailAlreadyExistsException`: 409 Conflict.
- `ResourceInUseException`: 409 Conflict (e.g., deleting a company with apps).
- `BadCredentialsException`: 401 Unauthorized.
- `MethodArgumentNotValidException`: 400 Bad Request (DTO validation errors).

## 2.7. Dashboard Business Logic
`DashboardService` calculates real-time statistics for the current user using Java Streams:
- `applicationsByStatus`: `Map<String, Long>` (Grouping by status).
- `applicationsByType`: `Map<String, Long>` (Grouping by type).
- Total counts for applications, documents, and companies.

## 2.8. Third-Party Job API Integration
Fetches real-time job listings for the public landing page.
- **External API:** [ArbeitNow Job Board API](https://www.arbeitnow.com/api/job-board-api)
- **Mapping:** `PublicJobService` transforms external JSON into `PublicJobResponse`.

# 3. Installation and running the app
## 3.1. Java & Maven installations
#### Install Java 21
Ensure JDK 21 is installed and `JAVA_HOME` is correctly set.
Verify: `java -version`

#### Maven
The project uses the **Maven Wrapper (`mvnw`)**. You do not need a global Maven installation.
Verify: `./mvnw -version`

## 3.2. DB setup
1.  **Install PostgreSQL.**
2.  **Create the Database:**
    ```bash
    createdb edujobapp
    ```
3.  **Default Credentials:**
    - **Username:** `postgres`
    - **Password:** `password`
    Adjust in `application.properties` or set environment variables if needed.

# 4. Roadmap & Future Features
- **New Role: Supervisor:** View and assist subordinates' progress.
- **Notification Service:** Email alerts for upcoming deadlines.
- **Admin Tools:** Password reset and supervisor-user assignment.

# 5. API Documentation
Swagger UI is fully integrated for interactive testing.
- **URL:** `http://localhost:8080/docs`
- **OpenAPI Spec:** `http://localhost:8080/api-docs`
