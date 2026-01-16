package lu.cnfpc.edujobapp.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ArbeitNowJob {
    @JsonProperty("slug")
    private String slug;
    
    @JsonProperty("company_name")
    private String companyName;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("remote")
    private boolean remote;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("tags")
    private List<String> tags;
    
    @JsonProperty("job_types")
    private List<String> jobTypes;
    
    @JsonProperty("location")
    private String location;
    
    @JsonProperty("created_at")
    private long createdAt;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<String> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
