package lu.cnfpc.edujobapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentRequest {

    @NotBlank(message = "File name is required")
    private String fileName;

    private String contentType;

    @NotNull(message = "Document status is required")
    private String docStatus; // Will map to EDocumentStatus enum name

    @NotNull(message = "User ID is required")
    private Long userId;

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
