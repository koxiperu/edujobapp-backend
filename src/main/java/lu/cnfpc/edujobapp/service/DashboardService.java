package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.response.ApplicationDashboardResponse;
import lu.cnfpc.edujobapp.dto.response.CompanyDashboardResponse;
import lu.cnfpc.edujobapp.dto.response.DashboardResponse;
import lu.cnfpc.edujobapp.entity.Application;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.repository.ApplicationRepository;
import lu.cnfpc.edujobapp.repository.CompanyRepository;
import lu.cnfpc.edujobapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserService userService;

    public DashboardResponse getUserStats() {
        User user = userService.getCurrentUser();
        Long userId = user.getId();

        List<Application> applications = applicationRepository.findByUserId(userId);
        long totalDocuments = documentRepository.countByUserId(userId);
        long totalCompanies = companyRepository.countByUserId(userId);
        
        long totalApplications = applications.size();

        Map<String, Long> statusStats = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getAppStatus().name(), Collectors.counting()));

        Map<String, Long> typeStats = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getApplicationType().name(), Collectors.counting()));
                
        // Map to ApplicationDashboardResponse
        List<ApplicationDashboardResponse> allApplications = applications.stream()
                .map(this::toDashboardDto)
                .collect(Collectors.toList());

        return new DashboardResponse(totalApplications, totalDocuments, totalCompanies, statusStats, typeStats, allApplications);
    }

    private ApplicationDashboardResponse toDashboardDto(Application app) {
        ApplicationDashboardResponse dto = new ApplicationDashboardResponse();
        dto.setId(app.getId());
        dto.setTitle(app.getTitle());
        dto.setDescription(app.getDescription());
        dto.setApplicationType(app.getApplicationType().name());
        dto.setCreationDate(app.getCreationDate());
        dto.setSubmitDate(app.getSubmitDate());
        dto.setSubmitDeadline(app.getSubmitDeadline());
        dto.setResponseDeadline(app.getResponseDeadline());
        dto.setAppStatus(app.getAppStatus().name());
        dto.setResultNotes(app.getResponseNotes());
        dto.setUserId(app.getUser().getId());
        
        if (app.getCompany() != null) {
            dto.setCompany(new CompanyDashboardResponse(
                app.getCompany().getName(),
                app.getCompany().getCountry()
            ));
        }
        return dto;
    }
}
