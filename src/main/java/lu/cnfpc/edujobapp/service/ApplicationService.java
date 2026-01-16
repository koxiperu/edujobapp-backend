package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.request.ApplicationRequest;
import lu.cnfpc.edujobapp.dto.response.ApplicationResponse;
import lu.cnfpc.edujobapp.entity.Application;
import lu.cnfpc.edujobapp.entity.Company;
import lu.cnfpc.edujobapp.entity.Document;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.entity.enums.EApplicationStatus;
import lu.cnfpc.edujobapp.entity.enums.EApplicationType;
import lu.cnfpc.edujobapp.mapper.ApplicationMapper;
import lu.cnfpc.edujobapp.repository.ApplicationRepository;
import lu.cnfpc.edujobapp.repository.CompanyRepository;
import lu.cnfpc.edujobapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserService userService;

    public List<ApplicationResponse> getMyApplications() {
        User user = userService.getCurrentUser();
        return applicationRepository.findByUserId(user.getId()).stream()
                .map(applicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }
    
    public List<ApplicationResponse> getApplicationsByCompany(Long companyId) {
        // Verify company exists
        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found with id: " + companyId);
        }
        // Check permissions (User should own the company or be Admin? Or user owns applications?)
        // Requirement says: "Get all applications for a specific institution." 
        // Logic: Return applications linked to this company.
        // Assuming this is called by CompanyController for "Institutions for logged-in user".
        // So the user should own the company.
        
        return applicationRepository.findByCompanyId(companyId).stream()
                .map(applicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> getApplicationsByDocument(Long documentId) {
         if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Document not found with id: " + documentId);
        }
        return applicationRepository.findByDocumentsId(documentId).stream()
                .map(applicationMapper::toApplicationResponse)
                .collect(Collectors.toList());
    }

    public ApplicationResponse createApplication(ApplicationRequest request) {
        User user = userService.getCurrentUser();
        
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.getCompanyId()));

        Application application = new Application();
        application.setTitle(request.getTitle());
        application.setDescription(request.getDescription());
        try {
            application.setApplicationType(EApplicationType.valueOf(request.getApplicationType()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid application type: " + request.getApplicationType());
        }
        
        application.setSubmitDate(request.getSubmitDate());
        application.setSubmitDeadline(request.getSubmitDeadline());
        application.setResponseDeadline(request.getResponseDeadline());
        
        try {
            application.setAppStatus(EApplicationStatus.valueOf(request.getAppStatus()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid application status: " + request.getAppStatus());
        }
        
        application.setResponseNotes(request.getResultNotes());
        application.setUser(user);
        application.setCompany(company);
        
        if (request.getDocumentIds() != null && !request.getDocumentIds().isEmpty()) {
            Set<Document> documents = new HashSet<>(documentRepository.findAllById(request.getDocumentIds()));
            application.setDocuments(documents);
        }
        
        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }

    public ApplicationResponse getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!application.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to access this application.");
        }
        
        return applicationMapper.toApplicationResponse(application);
    }

    public ApplicationResponse updateApplication(Long id, ApplicationRequest request) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!application.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to update this application.");
        }

        application.setTitle(request.getTitle());
        application.setDescription(request.getDescription());
        
        if (request.getApplicationType() != null) {
            try {
                application.setApplicationType(EApplicationType.valueOf(request.getApplicationType()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid application type: " + request.getApplicationType());
            }
        }
        
        application.setSubmitDate(request.getSubmitDate());
        application.setSubmitDeadline(request.getSubmitDeadline());
        application.setResponseDeadline(request.getResponseDeadline());
        
        if (request.getAppStatus() != null) {
             try {
                application.setAppStatus(EApplicationStatus.valueOf(request.getAppStatus()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid application status: " + request.getAppStatus());
            }
        }

        application.setResponseNotes(request.getResultNotes());
        
        // Update company if changed
        if (request.getCompanyId() != null && !request.getCompanyId().equals(application.getCompany().getId())) {
             Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.getCompanyId()));
             application.setCompany(company);
        }
        
        // Update documents
        if (request.getDocumentIds() != null) {
             Set<Document> documents = new HashSet<>(documentRepository.findAllById(request.getDocumentIds()));
             application.setDocuments(documents);
        }

        return applicationMapper.toApplicationResponse(applicationRepository.save(application));
    }

    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        if (!application.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to delete this application.");
        }
        
        applicationRepository.delete(application);
    }
}
