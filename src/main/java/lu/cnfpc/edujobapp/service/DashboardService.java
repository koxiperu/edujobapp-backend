package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.response.ApplicationResponse;
import lu.cnfpc.edujobapp.dto.response.DashboardResponse;
import lu.cnfpc.edujobapp.entity.Application;
import lu.cnfpc.edujobapp.entity.Company;
import lu.cnfpc.edujobapp.entity.Document;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.mapper.ApplicationMapper;
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
    
    @Autowired
    private ApplicationMapper applicationMapper;

    public DashboardResponse getUserStats() {
        User user = userService.getCurrentUser();
        Long userId = user.getId();

        List<Application> applications = applicationRepository.findByUserId(userId);
        List<Document> documents = documentRepository.findByUserId(userId);
        List<Company> companies = companyRepository.findByUserId(userId);
        
        long totalApplications = applications.size();
        long totalDocuments = documents.size();
        long totalCompanies = companies.size();

        Map<String, Long> statusStats = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getAppStatus().name(), Collectors.counting()));

        Map<String, Long> typeStats = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getApplicationType().name(), Collectors.counting()));
                
        // Extract names
        List<String> documentNames = documents.stream()
                .map(Document::getFileName)
                .collect(Collectors.toList());
                
        List<String> companyNames = companies.stream()
                .map(Company::getName)
                .collect(Collectors.toList());
                
        // Map full applications
        List<ApplicationResponse> allApplications = applications.stream()
                .map(applicationMapper::toApplicationResponse)
                .collect(Collectors.toList());

        return new DashboardResponse(totalApplications, totalDocuments, totalCompanies, statusStats, typeStats, documentNames, companyNames, allApplications);
    }
}
