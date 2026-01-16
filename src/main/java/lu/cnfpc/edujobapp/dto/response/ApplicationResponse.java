package lu.cnfpc.edujobapp.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class ApplicationResponse {

    private Long id;
    private String title;
    private String description;
    private String applicationType; // Will map to EApplicationType enum name
    private LocalDateTime creationDate;
    private LocalDate submitDate;
    private LocalDate submitDeadline;
    private LocalDate responseDeadline;
    private String appStatus; // Will map to EApplicationStatus enum name
    private String resultNotes;// Nested DTO
    private CompanyResponse company; // Nested DTO
    private Set<DocumentResponse> documents; // Nested DTO

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
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

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getResultNotes() {
        return resultNotes;
    }

    public void setResultNotes(String resultNotes) {
        this.resultNotes = resultNotes;
    }

    public CompanyResponse getCompany() {
        return company;
    }

    public void setCompany(CompanyResponse company) {
        this.company = company;
    }

    public Set<DocumentResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentResponse> documents) {
        this.documents = documents;
    }
}