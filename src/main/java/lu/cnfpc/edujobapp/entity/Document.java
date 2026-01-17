package lu.cnfpc.edujobapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lu.cnfpc.edujobapp.entity.enums.EDocumentStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fileName;

    private String contentType;

    @CreationTimestamp
    private LocalDateTime uploadDate;

    @Lob
    private byte[] data;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EDocumentStatus docStatus;

    // Many-to-One relationship with User
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-Many relationship with Application
    @ManyToMany(mappedBy = "documents")
    private Set<Application> applications = new HashSet<>();

    //Constructors
    public Document() {
    }
    public Document(String fileName, String contentType, byte[] data,
                    EDocumentStatus docStatus, User user) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.docStatus = docStatus;
        this.user = user;
    }
    //Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
    public EDocumentStatus getDocStatus() {
        return docStatus;
    }
    public void setDocStatus(EDocumentStatus docStatus) {
        this.docStatus = docStatus;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Set<Application> getApplications() {
        return applications;
    }
    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }
    
}
