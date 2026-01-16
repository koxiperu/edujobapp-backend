package lu.cnfpc.edujobapp.mapper;

import lu.cnfpc.edujobapp.dto.response.ApplicationResponse;
import lu.cnfpc.edujobapp.entity.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ApplicationMapper {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private DocumentMapper documentMapper;

    public ApplicationResponse toApplicationResponse(Application application) {
        if (application == null) {
            return null;
        }
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setTitle(application.getTitle());
        response.setDescription(application.getDescription());
        if (application.getApplicationType() != null) {
            response.setApplicationType(application.getApplicationType().name());
        }
        response.setCreationDate(application.getCreationDate());
        response.setSubmitDate(application.getSubmitDate());
        response.setSubmitDeadline(application.getSubmitDeadline());
        response.setResponseDeadline(application.getResponseDeadline());
        if (application.getAppStatus() != null) {
            response.setAppStatus(application.getAppStatus().name());
        }
        response.setResultNotes(application.getResponseNotes());
        
        if (application.getUser() != null) {
            response.setUserId(application.getUser().getId());
        }
        if (application.getCompany() != null) {
            response.setCompany(companyMapper.toCompanyResponse(application.getCompany()));
        }
        
        if (application.getDocuments() != null) {
            response.setDocuments(application.getDocuments().stream()
                    .map(documentMapper::toDocumentResponse)
                    .collect(Collectors.toSet()));
        }
        
        return response;
    }
}