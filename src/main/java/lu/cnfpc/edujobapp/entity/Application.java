package lu.cnfpc.edujobapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lu.cnfpc.edujobapp.entity.enums.EApplicationStatus;
import lu.cnfpc.edujobapp.entity.enums.EApplicationType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String title;
    
    @Lob
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EApplicationType applicationType;

    private LocalDateTime creationDate = LocalDateTime.now();

    private LocalDate submitDate;

    private LocalDate submitDeadline;

    private LocalDate responseDeadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EApplicationStatus appStatus;

    @Lob
    private String responseNotes;

    // Many-to-One relationship with User
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-One relationship with Company
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "app_doc", joinColumns = @JoinColumn(name = "application_id"), inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents = new HashSet<>();

    public Application() {
    }

    public Application(
            Long id,
            String title,
            EApplicationType applicationType,
            LocalDateTime creationDate,
            LocalDate submitDate,
            LocalDate submitDeadline,
            LocalDate responseDeadline,
            EApplicationStatus appStatus,
            String description,
            String resultNotes,
            User user,
            Company company,
            Set<Document> documents) {
        this.id = id;
        this.title = title;
        this.applicationType = applicationType;
        this.creationDate = creationDate;
        this.submitDate = submitDate;
        this.submitDeadline = submitDeadline;
        this.responseDeadline = responseDeadline;
        this.appStatus = appStatus;
        this.description = description;
        this.responseNotes = resultNotes; // Changed to match field name
        this.user = user;
        this.company = company;
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(EApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDate submitDate) {
        this.submitDate = submitDate;
    }

    public LocalDate getSubmitDeadline() {
        return submitDeadline;
    }

    public void setSubmitDeadline(LocalDate submitDeadline) {
        this.submitDeadline = submitDeadline;
    }

    public LocalDate getResponseDeadline() {
        return responseDeadline;
    }

    public void setResponseDeadline(LocalDate responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public EApplicationStatus getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(EApplicationStatus appStatus) {
        this.appStatus = appStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponseNotes() {
        return responseNotes;
    }

    public void setResponseNotes(String responseNotes) {
        this.responseNotes = responseNotes;
    }
    
    // Kept for backward compatibility if needed by DTOs mapping to "resultNotes" property, 
    // but cleaner to rename DTO property. 
    // However, the original code had "responseNotes" field and "resultNotes" getter/setter. 
    // I am standardizing on "responseNotes" field and methods.
    // I will verify DTO usage.
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}