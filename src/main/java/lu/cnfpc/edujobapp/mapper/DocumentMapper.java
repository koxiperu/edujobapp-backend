package lu.cnfpc.edujobapp.mapper;

import lu.cnfpc.edujobapp.dto.response.DocumentResponse;
import lu.cnfpc.edujobapp.entity.Document;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class DocumentMapper {

    public DocumentResponse toDocumentResponse(Document document) {
        if (document == null) {
            return null;
        }
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setFileName(document.getFileName());
        response.setContentType(document.getContentType());
        response.setUploadDate(document.getUploadDate());
        if (document.getDocStatus() != null) {
            response.setDocStatus(document.getDocStatus().name());
        }
        if (document.getUser() != null) {
            response.setUserId(document.getUser().getId());
        }
        
        // Generate download URL
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/documents/")
                .path(document.getId().toString())
                .path("/download")
                .toUriString();
        response.setDownloadUrl(downloadUrl);

        if (document.getApplications() != null) {
            response.setApplicationCount(document.getApplications().size());
        }
        
        return response;
    }
}
