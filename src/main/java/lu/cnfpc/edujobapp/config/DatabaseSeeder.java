package lu.cnfpc.edujobapp.config;

import lu.cnfpc.edujobapp.entity.Application;
import lu.cnfpc.edujobapp.entity.Company;
import lu.cnfpc.edujobapp.entity.Document;
import lu.cnfpc.edujobapp.entity.Role;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.entity.enums.EApplicationStatus;
import lu.cnfpc.edujobapp.entity.enums.EApplicationType;
import lu.cnfpc.edujobapp.entity.enums.ECompanyType;
import lu.cnfpc.edujobapp.entity.enums.EDocumentStatus;
import lu.cnfpc.edujobapp.entity.enums.ERole;
import lu.cnfpc.edujobapp.repository.ApplicationRepository;
import lu.cnfpc.edujobapp.repository.CompanyRepository;
import lu.cnfpc.edujobapp.repository.DocumentRepository;
import lu.cnfpc.edujobapp.repository.RoleRepository;
import lu.cnfpc.edujobapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final DocumentRepository documentRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoleRepository roleRepository,
                          UserRepository userRepository,
                          CompanyRepository companyRepository,
                          DocumentRepository documentRepository,
                          ApplicationRepository applicationRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.documentRepository = documentRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedCompanies();
        seedDocuments();
        seedApplications();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.USER.name()));
            roleRepository.save(new Role(ERole.ADMIN.name()));
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName(ERole.ADMIN.name())
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
            Role userRole = roleRepository.findByName(ERole.USER.name())
                    .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));

            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin"),
                    "admin@ab.com",
                    "admin_name",
                    "admin_surname",
                    LocalDate.of(2000, 1, 1),
                    null, // Phone is optional
                    adminRole
            );
            userRepository.save(admin);

            User test = new User(
                    "test",
                    passwordEncoder.encode("test"),
                    "test@ab.com",
                    "test_name",
                    "test_surname",
                    LocalDate.of(2000, 1, 2),
                    "123456789",
                    userRole
            );
            userRepository.save(test);
        }
    }

    private void seedCompanies() {
        if (companyRepository.count() == 0) {
            User test = userRepository.findByUsername("test").orElse(null);

            List<String> companyNames = Arrays.asList(
                "CFL - Société Nationale des Chemins de Fer Luxembourgeois",
                "Dussmann Luxembourg",
                "POST Luxembourg",
                "Amazon",
                "Cactus",
                "BNP PARIBAS Luxembourg",
                "PwC Luxembourg",
                "ArcelorMittal",
                "Goodyear",
                "Cargolux Airlines International SA"
            );

            if (test != null) {
                for (int i = 0; i < companyNames.size(); i++) {
                    String name = companyNames.get(i);
                    Company company = new Company();
                    company.setName(name);
                    company.setType(ECompanyType.EMPLOYER);
                    company.setCountry("Luxembourg");
                    company.setUser(test);
                    
                    // Generate dummy data
                    String safeName = name.split(" ")[0].toLowerCase().replaceAll("[^a-z]", "");
                    company.setAddress("Rue " + safeName + " " + (i + 1) + ", L-" + (1000 + i * 100) + " Luxembourg");
                    company.setEmail("contact@" + safeName + ".lu");
                    company.setPhone("+352 20 " + (100 + i) + " " + (100 + i));
                    company.setWebsite("https://www." + safeName + ".lu");
                    
                    companyRepository.save(company);
                }
            }
        }
    }

    private void seedDocuments() {
        if (documentRepository.count() == 0) {
            User test = userRepository.findByUsername("test")
                    .orElseThrow(() -> new RuntimeException("Test user not found for document seeding."));
            String seedDocumentsPath = "src/main/java/lu/cnfpc/edujobapp/config/seed_documents";
            java.io.File folder = new java.io.File(seedDocumentsPath);
            java.io.File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (java.io.File file : listOfFiles) {
                    if (file.isFile()) {
                        try {
                            byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
                            String fileName = file.getName();
                            String contentType = getContentType(fileName);

                            Document document = new Document(
                                    fileName,
                                    contentType,
                                    fileContent,
                                    EDocumentStatus.READY,
                                    test
                            );
                            documentRepository.save(document);
                        } catch (java.io.IOException e) {
                            System.err.println("Error reading file: " + file.getName() + " - " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void seedApplications() {
        if (applicationRepository.count() == 0) {
            User test = userRepository.findByUsername("test")
                    .orElseThrow(() -> new RuntimeException("Test user not found for application seeding."));

            List<Company> companies = companyRepository.findAll();
            List<Document> documents = documentRepository.findAll();

            if (!companies.isEmpty() && !documents.isEmpty()) {
                // Application 1 for test: Job at CFL, submitted
                Application app1 = new Application();
                app1.setTitle("Software Engineer Intern");
                app1.setDescription("Internship position for a software engineer at CFL.");
                app1.setApplicationType(EApplicationType.JOB);
                app1.setSubmitDate(LocalDate.now().minusDays(10));
                app1.setSubmitDeadline(LocalDate.now().plusDays(20));
                app1.setResponseDeadline(LocalDate.now().plusDays(30));
                app1.setAppStatus(EApplicationStatus.SUBMITTED);
                app1.setUser(test);
                app1.setCompany(companies.get(0)); // CFL
                app1.setDocuments(new HashSet<>(Arrays.asList(documents.get(0), documents.get(1)))); // CV and Motivation letter
                applicationRepository.save(app1);

                // Application 2 for test: University application, accepted
                Application app2 = new Application();
                app2.setTitle("Master in Computer Science");
                app2.setDescription("Application for Master's program at University.");
                app2.setApplicationType(EApplicationType.UNIVERSITY);
                app2.setSubmitDate(LocalDate.now().minusMonths(2));
                app2.setSubmitDeadline(LocalDate.now().minusMonths(1));
                app2.setResponseDeadline(LocalDate.now().minusWeeks(1));
                app2.setAppStatus(EApplicationStatus.ACCEPTED);
                app2.setUser(test);
                app2.setCompany(companies.get(1)); // Dussmann (just for example, ideally a university)
                app2.setDocuments(new HashSet<>(Collections.singletonList(documents.get(2)))); // Developer_Description.pdf
                applicationRepository.save(app2);

                // Application 3 for test: Job at POST, planned -> DRAFT
                Application app3 = new Application();
                app3.setTitle("Data Analyst Position");
                app3.setDescription("Seeking a data analyst role at POST Luxembourg.");
                app3.setApplicationType(EApplicationType.JOB);
                app3.setSubmitDeadline(LocalDate.now().plusWeeks(2));
                app3.setAppStatus(EApplicationStatus.DRAFT);
                app3.setUser(test);
                app3.setCompany(companies.get(2)); // POST Luxembourg
                app3.setDocuments(new HashSet<>(Collections.singletonList(documents.get(3)))); // Interview.txt (as a placeholder)
                applicationRepository.save(app3);

                // Application 4 for test: Course application, rejected
                Application app4 = new Application();
                app4.setTitle("Advanced Java Programming Course");
                app4.setDescription("Application for a specialized Java course.");
                app4.setApplicationType(EApplicationType.COURSE);
                app4.setSubmitDate(LocalDate.now().minusMonths(1));
                app4.setSubmitDeadline(LocalDate.now().plusDays(5));
                app4.setResponseDeadline(LocalDate.now().plusDays(15));
                app4.setAppStatus(EApplicationStatus.REJECTED);
                app4.setResponseNotes("Overqualified for the course.");
                app4.setUser(test);
                app4.setCompany(companies.get(3)); // Amazon (just for example)
                app4.setDocuments(new HashSet<>(Arrays.asList(documents.get(4), documents.get(5)))); // Motivation letter, CV
                applicationRepository.save(app4);
                
                // Application 5: Job at Cactus, Interview -> UNDER_REVIEW
                Application app5 = new Application();
                app5.setTitle("Store Manager");
                app5.setDescription("Management position at Cactus.");
                app5.setApplicationType(EApplicationType.JOB);
                app5.setSubmitDate(LocalDate.now().minusDays(5));
                app5.setSubmitDeadline(LocalDate.now().plusDays(25));
                app5.setResponseDeadline(LocalDate.now().plusDays(35));
                app5.setAppStatus(EApplicationStatus.UNDER_REVIEW);
                app5.setUser(test);
                app5.setCompany(companies.get(4)); // Cactus
                app5.setDocuments(new HashSet<>(Collections.singletonList(documents.get(0))));
                applicationRepository.save(app5);
                
                // Application 6: Job at BNP Paribas, Offer Received -> ACCEPTED (or UNDER_REVIEW)
                Application app6 = new Application();
                app6.setTitle("Financial Advisor");
                app6.setDescription("Advising clients on financial investments.");
                app6.setApplicationType(EApplicationType.JOB);
                app6.setSubmitDate(LocalDate.now().minusMonths(3));
                app6.setAppStatus(EApplicationStatus.ACCEPTED); 
                app6.setUser(test);
                app6.setCompany(companies.get(5)); // BNP
                app6.setDocuments(new HashSet<>(Arrays.asList(documents.get(1), documents.get(5))));
                applicationRepository.save(app6);
                
                // Application 7: Course at PwC, Waiting -> UNDER_REVIEW
                Application app7 = new Application();
                app7.setTitle("Taxation Law Workshop");
                app7.setDescription("Workshop on Luxembourg taxation laws.");
                app7.setApplicationType(EApplicationType.COURSE);
                app7.setSubmitDate(LocalDate.now().minusDays(2));
                app7.setAppStatus(EApplicationStatus.UNDER_REVIEW);
                app7.setUser(test);
                app7.setCompany(companies.get(6)); // PwC
                app7.setDocuments(new HashSet<>(Collections.singletonList(documents.get(4))));
                applicationRepository.save(app7);
                
                // Application 8: Job at ArcelorMittal, Planned -> DRAFT
                Application app8 = new Application();
                app8.setTitle("Metallurgist");
                app8.setDescription("Research position in metallurgy.");
                app8.setApplicationType(EApplicationType.JOB);
                app8.setSubmitDeadline(LocalDate.now().plusMonths(1));
                app8.setAppStatus(EApplicationStatus.DRAFT);
                app8.setUser(test);
                app8.setCompany(companies.get(7)); // ArcelorMittal
                app8.setDocuments(new HashSet<>(Collections.singletonList(documents.get(0))));
                applicationRepository.save(app8);
            }
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return "application/msword";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return "application/vnd.ms-excel";
        }
        return "application/octet-stream";
    }
}