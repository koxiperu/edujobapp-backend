package lu.cnfpc.edujobapp.service;

import lu.cnfpc.edujobapp.dto.external.ArbeitNowJob;
import lu.cnfpc.edujobapp.dto.external.ArbeitNowResponse;
import lu.cnfpc.edujobapp.dto.response.PublicJobResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicJobService {

    @Value("${external.api.jobs.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;

    public PublicJobService() {
        this.restTemplate = new RestTemplate();
    }

    public List<PublicJobResponse> getPublicJobs() {
        try {
            ArbeitNowResponse response = restTemplate.getForObject(apiUrl, ArbeitNowResponse.class);
            if (response != null && response.getData() != null) {
                return response.getData().stream()
                        .map(this::mapToPublicJobResponse)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // Log error in a real app
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private PublicJobResponse mapToPublicJobResponse(ArbeitNowJob job) {
        return new PublicJobResponse(
                job.getCompanyName(),
                job.getTitle(),
                job.getUrl(),
                job.isRemote(),
                job.getJobTypes(),
                job.getTags(),
                job.getLocation()
        );
    }
}
