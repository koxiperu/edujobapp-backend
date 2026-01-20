package lu.cnfpc.edujobapp.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private long totalApplications;
    private long totalDocuments;
    private long totalCompanies;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> applicationsByType;
    private List<ApplicationDashboardResponse> allApplications;

    public DashboardResponse() {
    }

    public DashboardResponse(long totalApplications, long totalDocuments, long totalCompanies, 
                             Map<String, Long> applicationsByStatus, Map<String, Long> applicationsByType,
                             List<ApplicationDashboardResponse> allApplications) {
        this.totalApplications = totalApplications;
        this.totalDocuments = totalDocuments;
        this.totalCompanies = totalCompanies;
        this.applicationsByStatus = applicationsByStatus;
        this.applicationsByType = applicationsByType;
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

    public List<ApplicationDashboardResponse> getAllApplications() {
        return allApplications;
    }

    public void setAllApplications(List<ApplicationDashboardResponse> allApplications) {
        this.allApplications = allApplications;
    }
}