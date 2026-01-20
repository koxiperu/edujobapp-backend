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

# 2. Steps of creation backend (For Developer)
## 2.1. Create empty project with essential dependencies:
Initialize a Spring Boot project using Spring Initializr or your IDE. Include the following dependencies to support web development, database interaction, security, validation, and API documentation:
- `spring-boot-starter-web`: For building web, including RESTful, applications using Spring MVC.
- `spring-boot-starter-data-jpa`: For using Spring Data JPA with Hibernate.
- `spring-boot-starter-validation`: For using Java Bean Validation with Hibernate Validator.
- `spring-boot-starter-security`: For using Spring Security.
- `postgresql`: PostgreSQL JDBC driver for database connectivity.
- `jjwt` (api, impl, jackson): For creating and verifying JWT (JSON Web Token) for stateless authentication.
- `springdoc-openapi-starter-webmvc-ui`: For generating interactive API documentation with Swagger UI.
- `spring-boot-starter-test`: For testing Spring Boot applications.

and folder structure:
```
/src
├───main
│   ├───java
│   │   └───lu
│   │       └───cnfpc
│   │           └───edujobapp
│   │               ├───config
│   │               ├───controller
│   │               ├───dto
│   │               │   ├───error
│   │               │   ├───request
│   │               │   └───response
│   │               ├───entity
│   │               │   └───enums
│   │               ├───exception
│   │               ├───mapper
│   │               ├───repository
│   │               ├───security
│   │               └───service
│   │                   └───auth
│   └───resources
└───test
    └───java
```
## 2.2. Configure application.properties (for Oracle or Postgres)
## 2.3. Create Entities
## 2.4. Create seeds for DB initialization
Create Repositories classes.
#### roles table
1. USER
2. ADMIN
#### users table
1. Admin user:
- username: admin
- password: admin
- email: admin@ab.com
- first_name: admin_name
- last_name: admin_surname
- birthdate: 1.1.2000
- role: admin 
2. Test user:
- username: test
- password: test
- email: test@ab.com
- first_name: test_name
- last_name: test_surname
- birthdate: 2.1.2000
- phone: 123456789
- role: user
#### companies table:
Default companies for existing user (test):
- "CFL - Société Nationale des Chemins de Fer Luxembourgeois",
- "Dussmann Luxembourg",
- "POST Luxembourg",
- "Amazon",
- "Cactus",
- "BNP PARIBAS Luxembourg",
- "PwC Luxembourg",
- "ArcelorMittal",
- "Goodyear",
- "Cargolux Airlines International SA"
All in country Luxembourg. Other data define randomly.
#### documents table
1. Take documents from /config/seed_documents, apply to the test user.
#### applications table
Create different applications for the test user, and with different documents attached and statuses.
## 2.5. Create DTOs (Data Transfer Objects)
Create separate classes to handle data transfer between client and server, ensuring that internal entity structures (like `User` passwords) are not exposed directly. Implement `Request` DTOs for input validation and `Response` DTOs for structured output.

## 2.6. Create controllers, services, repositories
Implement the 3-layer architecture:
- **Repository Layer:** Interfaces extending `JpaRepository` for DB access.
- **Service Layer:** Classes containing business logic (e.g., `UserService`, `ApplicationService`), handling validations and transactions.
- **Controller Layer:** REST Controllers (`@RestController`) defining API endpoints, handling HTTP requests, and mapping DTOs. Use `Mappers` to convert between Entities and DTOs.

## 2.7. Add JWT and security
Configure `WebSecurityConfig` to secure endpoints. Implement `JwtService` to generate and validate tokens. Create `JwtAuthenticationFilter` to intercept requests and set the `UserPrincipal` in the security context. Enable CORS and Swagger UI access.

## 2.8. Integrate Third-Party Job API
Enhance the landing page for unauthenticated users by displaying real-time job advertisements fetched from an external API. This feature provides immediate value and encourages user registration for full application tracking capabilities.

**Integration Details:**
- **External API:** [ArbeitNow Job Board API](https://www.arbeitnow.com/api/job-board-api)
- **Implementation:** Develop a service to consume the external JSON data and transform it into a standardized internal DTO.
- **Data Mapping:** The backend exposes a public endpoint (`/api/public/jobs`) returning a list of job objects with the following fields:
  - `company_name`: Name of the hiring organization.
  - `title`: Job position title.
  - `url`: Direct link to the job posting.
  - `remote`: Boolean indicating if the position is remote.
  - `job_types`: List of employment types (e.g., Full-time, Internship).
  - `tags`: List of relevant keywords or technologies.
  - `location`: Geographic location of the job.

# 3. Installation and running the app
## 3.1. Java & Maven installations
#### Install Java 21
Windows:
- Download JDK 21 (LTS) from:
https://adoptium.net/
- Choose:
Version: 21
Package: JDK
OS: Windows
- Install and check “Set JAVA_HOME”
- Verify:
```
java -version
```

macOS (Homebrew):
```
brew install openjdk@21
echo 'export JAVA_H$(/usr/libexec/java_home -v21)' >> ~/.zshrc
source ~/.zshrc
java -version
```

Linux (Ubuntu):
```
sudo apt update
sudo apt install -y openjdk-21-jdk
java -version
```

#### Install Maven
Windows:
- Download Maven:
https://maven.apache.org/download.cgi
- Extract to: C:\Program Files\Apache\Maven
- Add to PATH: C:\Program Files\Apache\Maven\bin
- Verify:
```
mvn -version
```

macOS (Homebrew):
```
brew install maven
mvn -version
```

Linux (Ubuntu):
```
sudo apt install -y maven
mvn -version
```
## 3.2. DB setup
To run the application, you need to have a PostgreSQL database server installed and running.

#### Install PostgreSQL and pgAdmin (Optional)
   *   **PostgreSQL:** Follow the official PostgreSQL installation guide for your operating system.
       [https://www.postgresql.org/download/](https://www.postgresql.org/download/)
   *   **pgAdmin (Optional):** pgAdmin is a popular graphical administration tool for PostgreSQL.
       [https://www.pgadmin.org/download/](https://www.pgadmin.org/download/)

#### Create the 'edujobapp' Database
   Once PostgreSQL is installed, create a new database named `edujobapp`. You can do this using either the command line or pgAdmin.

   **Using Command Line (recommended for quick setup):**
   Open your terminal or command prompt and run the following command (you might need to switch to the `postgres` user or use `sudo -u postgres`):
   ```bash
   createdb edujobapp
   ```
   If `createdb` is not in your PATH, you might need to specify the full path, e.g., `/usr/local/bin/createdb edujobapp`.

   Alternatively, you can use the `psql` interactive terminal:
   ```bash
   psql -U postgres
   # Enter your PostgreSQL password if prompted
   CREATE DATABASE edujobapp;
   \q
   ```

   **Using pgAdmin:**
   1.  Open pgAdmin and connect to your PostgreSQL server.
   2.  Right-click on "Databases" in the browser tree.
   3.  Select "Create" -> "Database...".
   4.  In the "Create - Database" dialog, enter `edujobapp` in the "Database name" field.
   5.  Click "Save".

#### Database Credentials
   Ensure you have the correct PostgreSQL username and password. By default, the app uses:
   ```properties
   username=postgres
   password=password
   ```
   Adjust these values if your PostgreSQL setup uses different credentials.

# 4. Roadmap & Future Features

## 4.1. New Role: Supervisor
- **Goal:** Allow supervisors to track and assist subordinates.
- **Features:**
    - **View Subordinates:** Access dashboard and stats of assigned users.
    - **Feedback:** Ability to comment on subordinates' applications or dashboard.
    - **Recommendations:** Create and assign lists of Jobs/Courses for subordinates.
    - **Subordinate View:** Users should see these recommended items on their own dashboard.

## 4.2. Admin Enhancements
- **Company Management:** Full CRUD access to manage all companies in the system.
- **User Security:** Functionality to change/reset user passwords.
- **Supervisor Management:** Assign users to supervisors.

## 4.3. Notification service
Sending emails about upcoming deadlines.

# 5. API Documentation

The full API documentation, including endpoints, request/response schemas, and interactive testing, is available via the Swagger UI once the application is running.

**URL:** `http://localhost:8080/docs`
**OpenAPI Spec:** `http://localhost:8080/api-docs`

To generate a PDF or static document:
1. Start the application.
2. Navigate to the OpenAPI Spec URL to download the `openapi.json` file.
3. Use an external tool (like Swagger Editor, SwaggerHub, or a CLI converter) to import the JSON and export it as PDF or HTML.