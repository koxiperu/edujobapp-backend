package lu.cnfpc.edujobapp.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private long totalApplications;
    private long totalDocuments;
    private long totalCompanies;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> applicationsByType;
    
    // New fields for frontend filtering
    private List<String> documentNames;
    private List<String> companyNames;
    private List<ApplicationResponse> allApplications;

    public DashboardResponse() {
    }

    public DashboardResponse(long totalApplications, long totalDocuments, long totalCompanies, 
                             Map<String, Long> applicationsByStatus, Map<String, Long> applicationsByType,
                             List<String> documentNames, List<String> companyNames, List<ApplicationResponse> allApplications) {
        this.totalApplications = totalApplications;
        this.totalDocuments = totalDocuments;
        this.totalCompanies = totalCompanies;
        this.applicationsByStatus = applicationsByStatus;
        this.applicationsByType = applicationsByType;
        this.documentNames = documentNames;
        this.companyNames = companyNames;
        this.allApplications = allApplications;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public Map<String, Long> getApplicationsByStatus() {
        return applicationsByStatus;
    }

    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) {
        this.applicationsByStatus = applicationsByStatus;
    }

    public Map<String, Long> getApplicationsByType() {
        return applicationsByType;
    }

    public void setApplicationsByType(Map<String, Long> applicationsByType) {
        this.applicationsByType = applicationsByType;
    }

    public List<String> getDocumentNames() {
        return documentNames;
    }

    public void setDocumentNames(List<String> documentNames) {
        this.documentNames = documentNames;
    }

    public List<String> getCompanyNames() {
        return companyNames;
    }

    public void setCompanyNames(List<String> companyNames) {
        this.companyNames = companyNames;
    }

    public List<ApplicationResponse> getAllApplications() {
        return allApplications;
    }

    public void setAllApplications(List<ApplicationResponse> allApplications) {
        this.allApplications = allApplications;
    }
}
