package lu.cnfpc.edujobapp.dto.response;

import java.util.List;

public class PublicJobResponse {
    private String company_name;
    private String title;
    private String url;
    private boolean remote;
    private List<String> job_types;
    private List<String> tags;
    private String location;

    public PublicJobResponse() {
    }

    public PublicJobResponse(String company_name, String title, String url, boolean remote, List<String> job_types, List<String> tags, String location) {
        this.company_name = company_name;
        this.title = title;
        this.url = url;
        this.remote = remote;
        this.job_types = job_types;
        this.tags = tags;
        this.location = location;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public List<String> getJob_types() {
        return job_types;
    }

    public void setJob_types(List<String> job_types) {
        this.job_types = job_types;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
