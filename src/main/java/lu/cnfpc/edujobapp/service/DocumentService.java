package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.request.DocumentRequest;
import lu.cnfpc.edujobapp.dto.response.DocumentResponse;
import lu.cnfpc.edujobapp.entity.Document;
import lu.cnfpc.edujobapp.entity.User;
import lu.cnfpc.edujobapp.entity.enums.EDocumentStatus;
import lu.cnfpc.edujobapp.mapper.DocumentMapper;
import lu.cnfpc.edujobapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private UserService userService;

    public List<DocumentResponse> getMyDocuments() {
        User user = userService.getCurrentUser();
        return documentRepository.findByUserId(user.getId()).stream()
                .map(documentMapper::toDocumentResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse uploadDocument(MultipartFile file, String fileName, String contentType, String docStatus) throws IOException {
        User user = userService.getCurrentUser();
        
        Document document = new Document();
        
        // Use provided fileName or fallback to original
        if (fileName != null && !fileName.isEmpty()) {
            document.setFileName(fileName);
        } else {
            document.setFileName(file.getOriginalFilename());
        }
        
        // Use provided contentType or fallback to file's contentType
        if (contentType != null && !contentType.isEmpty()) {
            document.setContentType(contentType);
        } else {
            document.setContentType(file.getContentType());
        }

        document.setData(file.getBytes());
        
        // Use provided status or fallback to READY
        if (docStatus != null && !docStatus.isEmpty()) {
             try {
                document.setDocStatus(EDocumentStatus.valueOf(docStatus));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid document status: " + docStatus);
            }
        } else {
            document.setDocStatus(EDocumentStatus.READY); // Default status
        }
        
        document.setUser(user);
        
        return documentMapper.toDocumentResponse(documentRepository.save(document));
    }

    public DocumentResponse getDocumentMetadata(Long id) {
        Document document = getDocumentEntity(id);
        return documentMapper.toDocumentResponse(document);
    }

    public Document getDocumentEntity(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        
        User currentUser = userService.getCurrentUser();
        // Allow access if owner or admin
        if (!document.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().getName().equals("ADMIN")) {
             throw new AccessDeniedException("You do not have permission to access this document.");
        }
        return document;
    }

    public DocumentResponse updateDocumentMetadata(Long id, DocumentRequest request) {
        Document document = getDocumentEntity(id);
        
        if (request.getFileName() != null) {
            document.setFileName(request.getFileName());
        }
        if (request.getDocStatus() != null) {
            try {
                document.setDocStatus(EDocumentStatus.valueOf(request.getDocStatus()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid document status: " + request.getDocStatus());
            }
        }
        
        return documentMapper.toDocumentResponse(documentRepository.save(document));
    }

    public void deleteDocument(Long id) {
        Document document = getDocumentEntity(id);

        if (!document.getApplications().isEmpty()) {
            throw new lu.cnfpc.edujobapp.exception.ResourceInUseException("Cannot delete document because it is associated with existing applications.");
        }

        documentRepository.delete(document);
    }
}
