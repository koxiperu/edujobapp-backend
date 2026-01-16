package lu.cnfpc.edujobapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lu.cnfpc.edujobapp.dto.request.DocumentRequest;
import lu.cnfpc.edujobapp.dto.response.ApplicationResponse;
import lu.cnfpc.edujobapp.dto.response.DocumentResponse;
import lu.cnfpc.edujobapp.entity.Document;
import lu.cnfpc.edujobapp.service.ApplicationService;
import lu.cnfpc.edujobapp.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "Document", description = "Document management APIs")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    
    @Autowired
    private ApplicationService applicationService;

    @Operation(summary = "Get all documents for the logged-in user")
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getMyDocuments() {
        return ResponseEntity.ok(documentService.getMyDocuments());
    }

    @Operation(summary = "Upload a new document")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(documentService.uploadDocument(file));
    }

    @Operation(summary = "Get metadata for a single document")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentMetadata(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocumentMetadata(id));
    }

    @Operation(summary = "Update a document's metadata")
    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocumentMetadata(@PathVariable Long id, @Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(documentService.updateDocumentMetadata(id, request));
    }

    @Operation(summary = "Delete a document")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Download a document")
    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentEntity(id); 
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }
    
    @Operation(summary = "Get all applications using a specific document")
    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByDocument(@PathVariable Long id) {
        // Ensure user has access to document first
        documentService.getDocumentMetadata(id); 
        return ResponseEntity.ok(applicationService.getApplicationsByDocument(id));
    }
}
