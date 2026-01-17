package lu.cnfpc.edujobapp.controller;

import lu.cnfpc.edujobapp.dto.response.PublicJobResponse;
import lu.cnfpc.edujobapp.service.PublicJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final PublicJobService publicJobService;

    @Autowired
    public PublicController(PublicJobService publicJobService) {
        this.publicJobService = publicJobService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<PublicJobResponse>> getPublicJobs() {
        List<PublicJobResponse> jobs = publicJobService.getPublicJobs();
        return ResponseEntity.ok(jobs);
    }
}
